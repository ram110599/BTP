/**
 * 
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.*;

import message.Message;
import message.MessageSendToSubscriber;

import database.DatabaseConnection;

/**
 * @author abhi
 *
 */
public class Server {

	/**
	 * 
	 */
	public Server() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param serverSocket
	 * @return DatagramPacket that it recieves from client(Publisher and Subscriber)
	 * @throws IOException
	 */
	public DatagramPacket receiveFromClient(DatagramSocket serverSocket) throws IOException{
		
		byte[] receiveData = new byte[1024];
	    DatagramPacket receivePacket =
	      new DatagramPacket(receiveData, receiveData.length);
	      
	    try{
	      serverSocket.receive(receivePacket);
	    }
	    catch(IOException e){
	      System.err.println(e);
	    }
	      return receivePacket;
		
	}
	
	// Function to send the response to client
	  public void sendToClient(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException{
	    try{
	      byte[] sendData  = new byte[1024];

	      String sentence = new String(receivePacket.getData());

	      InetAddress IPAddress = receivePacket.getAddress();
	      System.out.println("The address of the packet is " + IPAddress);

	      int port = receivePacket.getPort();

	      System.out.println("Port number of Publisher is " + port);

	      String capitalizedSentence = sentence.toUpperCase();

	      sendData = capitalizedSentence.getBytes();

	      DatagramPacket sendPacket =
	        new DatagramPacket(sendData, sendData.length, IPAddress, port);

	      serverSocket.send(sendPacket);

	    }
	    catch(IOException e){
	      System.err.println(e);
	    }
	    
	  }

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		// Variable for database connection
		String url = "jdbc:mysql://localhost:3306/publisher_subscriber";
	    String user = "root";
	    String password = "abhi12345";
	    
	    DatabaseConnection d = new DatabaseConnection(url, user, password);
	    
	    //	Port and Socket for server    
	    int serverPort = 9876;
	    final DatagramSocket serverSocket = new DatagramSocket(serverPort);
	    
	    // Object of UDPServer
	    Server serverObj = new Server();
	    System.out.println("Sever is listening.");

	    // No of consumer threads
	    int N_CONSUMERS = 5;
	    
	    // Message queue
	    final BlockingQueue<MessageSendToSubscriber> queue = new LinkedBlockingQueue<>();
	    
	    //
	    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_CONSUMERS);
	    
	    Runnable runnable = new Runnable() {
	        public void run() {
	          // task to run goes here
	          sendToSubscriber(queue, serverSocket);
	          }
	    };
	    
	    ScheduledExecutorService consumer = Executors.newScheduledThreadPool(N_CONSUMERS);
	      consumer.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
	      
	      while(true)
	        {
	          // Receive packet from the client and sent to the client
	          DatagramPacket receivePacket = serverObj.receiveFromClient(serverSocket);
	          System.out.println("Data received from client.");
	          serverObj.sendToClient(serverSocket, receivePacket);

	          // Port and ip address of the client
	          int publisherPort = receivePacket.getPort();
	          InetAddress pubIP = receivePacket.getAddress();
	          String publisherIP = pubIP.getHostAddress();
	          System.out.println("Publisher Ip is " + publisherIP);

	          // Message in the package
	          byte[] data = receivePacket.getData();
	          
	          // Have to verify this line
	          Message m = Message.getMessageFromByte(data);
	          System.out.println("Message is " + m.getMessage());

	          
	          executor.execute(new SubscriberKeeper(queue, d, publisherIP, publisherPort, m));
	          
	        }
	}

	
	/**
	 * @param messageQueue
	 * @param serverSocket
	 */
	public static void sendToSubscriber(BlockingQueue<MessageSendToSubscriber> messageQueue, DatagramSocket serverSocket){
		//	While Message Queue has messages in it	
		while(!messageQueue.isEmpty()){

	        MessageSendToSubscriber ms;
			try {
				ms = messageQueue.take();
			 
	        byte[] sendData  = new byte[1024];
	
	
	        // Getting the data from the message queue
	        Message m = ms.getMessage();
	        String sentence = m.getMessage();
	        InetAddress subscriberIP = ms.getSubscriberIP();
	        int subPort = ms.getSubPort();
	
	        String capitalizedSentence = sentence.toUpperCase();
	        m.setMessage(capitalizedSentence);
	        sendData = m.getBytes();
	
	        DatagramPacket sendPacket =
	          new DatagramPacket(sendData, sendData.length, subscriberIP, subPort);
	
			serverSocket.send(sendPacket);
			}catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	  }
	}

}
