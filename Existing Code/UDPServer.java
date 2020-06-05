import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.*;

// Class for storing the subscriber information
class MessageSendToSubscriber{
  
  // Message to be sent to subscriber
  String message;

  // Publisher Port and IP
  int pubPort;
  InetAddress publisherIP;

  // Subscriber Port and and IP
  int subPort;
  InetAddress subscriberIP;

  // Constructor of the class
  public MessageSendToSubscriber(){

  }

  public MessageSendToSubscriber(String message, int pubPort, InetAddress publisherIP, int subPort, InetAddress subscriberIP){
    this.message = message;
    this.pubPort = pubPort;
    this.publisherIP = publisherIP;
    this.subPort = subPort;
    this.subscriberIP = subscriberIP;
  }

  // Function to return the message
  public String getMessage(){
    return message;
  }

  // Function to return Subscriber Port
  public int getSubscriberPort(){
    return subPort;
  }

  // Function to return Subscriber IP
  public InetAddress getSubscriberIP(){
    return subscriberIP;
  }

  // Function to return the Publisher IP
  public InetAddress getPublisherIP(){
    return publisherIP;
  }

  // Function to return publisher Port
  public int getPublisherPort(){
    return pubPort;
  }

  // Function to set the message
  public void setMessage(String msg){
    message = msg;
  }

  // Function to set Subscriber Port
  public void setSubscriberPort(int subscriberPort){
    subPort = subscriberPort;
  }

  // Function to set Subscriber IP
  public void setSubscriberIP(InetAddress subIP){
    subscriberIP = subIP;
  }

  // Function to set the Publisher IP
  public void setPublisherIP(InetAddress pubIP){
    publisherIP = pubIP;
  }

  // Function to set publisher Port
  public void setPublisherPort(int publisherPort){
    pubPort = publisherPort;
  }

}

// class for keeping the subscriber in a queue i.e. Producer
class SubscriberKeeper implements Runnable {
  private BlockingQueue<MessageSendToSubscriber> messageQueue;
  private Connection conn;
  private String publisherIP;
  private int pubPort;
  private String message;

  // Constructor
  public SubscriberKeeper(BlockingQueue<MessageSendToSubscriber> messageQueue, Connection conn, int pubPort, String publisherIP, String message) {
        this.messageQueue = messageQueue;
        this.conn = conn;
        this.pubPort = pubPort;
        this.publisherIP = publisherIP;
        this.message = message;
    }

  // Overriding the run
  public void run() {
        try {
            // Adding subscriber in the queue
            addingSubscriber();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  // Adding subscriber from the query
  private void addingSubscriber(){
    
    // Executing the query
    String query = "SELECT DISTINCT sub_port, sub_ip FROM sub_info WHERE pub_ip = '"+ publisherIP + "' AND pub_port = " + pubPort +";";
    Statement stmt = null;
    
    try{
      stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(query);
      
      while(rs.next()){

        // Subscriber Port and IP
        int subPort = rs.getInt("sub_port");
        String subIP = rs.getString("sub_ip");
        // System.out.println("Subscriber Port is " + subPort + " and Subscriber IP is " + subIP);

        // Adding messageSendTosubscriber in queue
        
        try {
          MessageSendToSubscriber m = new MessageSendToSubscriber(message, pubPort, InetAddress.getByName(publisherIP), subPort, InetAddress.getByName(subIP));
          messageQueue.put(m);
          // System.out.println("Message is " + m.getMessage());
          // System.out.println("Subscriber IP is " + m.getSubscriberIP());
          // System.out.println("Subscriber Port is " + m.getSubscriberPort());
          // System.out.println("Publisher Port is " + m.getPublisherPort());
          // System.out.println("Publisher IP is " + m.getPublisherIP());
          // System.out.println("The size of messageQueue is " + messageQueue.size());
        }
        catch(UnknownHostException e){
          e.printStackTrace();
        }
        catch(InterruptedException e){
          e.printStackTrace();
        }
      }
      System.out.println("The size of messageQueue is " + messageQueue.size() + " in Producer thread.");
    }
    catch(SQLException e){
      e.printStackTrace();
    }
  }
}

// Class for sending the message to the subscriber i.e. consumer
class SendToSubscriber implements Runnable{
  private BlockingQueue<MessageSendToSubscriber> messageQueue;
  DatagramSocket serverSocket;

  // Constructor
  public SendToSubscriber(BlockingQueue<MessageSendToSubscriber> messageQueue, DatagramSocket serverSocket){
    this.messageQueue = messageQueue;
    this.serverSocket = serverSocket;
  }

  // Overriding the run
  public void run() {
        try {
            // Adding subscriber in the queue
            sendPacketToSubscriber();
            System.out.println("Current Thread " + Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  // Function to send the packets to subscriber
  private void sendPacketToSubscriber(){
    try{
      System.out.println("In consumer thread.");
      System.out.println("Size of the messageQueue is " + messageQueue.size());
      while(!messageQueue.isEmpty()){

        MessageSendToSubscriber m = messageQueue.take();
        byte[] sendData  = new byte[1024];


        // Getting the data from the message queue
        String sentence = m.message;
        InetAddress subscriberIP = m.subscriberIP;
        int subPort = m.subPort;

        String capitalizedSentence = sentence.toUpperCase();
        sendData = capitalizedSentence.getBytes();

        DatagramPacket sendPacket =
          new DatagramPacket(sendData, sendData.length, subscriberIP, subPort);

        serverSocket.send(sendPacket);
      }
      
    }
    catch(IOException e){
      System.err.println(e);
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
  }
}


class UDPServer {

  // Function for receive
  public DatagramPacket receiveFromClient(DatagramSocket serverSocket)throws IOException{
    
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
  // main function
  public static void main(String args[]) throws Exception
    {
      // Making the database connection using jdbc
      String url = "jdbc:mysql://localhost:3306/publisher_subscriber";
      String user = "root";
      String password = "abhi12345";

      Connection conn = DriverManager.getConnection(url, user, password);
      System.out.println("Connection Established with database.");

      // Port of server
      int serverPort = 9876;
      DatagramSocket serverSocket = new DatagramSocket(serverPort);
      
      // Object of UDPServer
      UDPServer serverObj = new UDPServer();
      System.out.println("Sever is listening.");

      // No of consumer threads
      int N_CONSUMERS = 5;

      // Message queue
      BlockingQueue<MessageSendToSubscriber> queue = new LinkedBlockingQueue<>();

      ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_CONSUMERS); 
      // Threadpool for putting the elements in the queue
      // ExecutorService producer = Executors.newSingleThreadExecutor();

      // Threadpool for sending packets to subscribers
      // ThreadPoolExecutor consumer = (ThreadPoolExecutor) Executors.newFixedThreadPool(N_CONSUMERS); 
      
      // Running the consumer function at a fixed interval
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
          String sentence = new String(receivePacket.getData());
          System.out.println("Message is " + sentence);

          // try {
            // consumer.execute(new SendToSubscriber(queue, serverSocket));
            // producer.execute(new SubscriberKeeper(queue, conn, publisherPort, publisherIP, sentence));

            executor.execute(new SubscriberKeeper(queue, conn, publisherPort, publisherIP, sentence));
            // executor.execute(new SendToSubscriber(queue, serverSocket));
        }
    }
    public static void sendToSubscriber(BlockingQueue<MessageSendToSubscriber> messageQueue, DatagramSocket serverSocket){
    try{
      // System.out.println("In consumer thread.");
      // System.out.println("Size of the messageQueue is " + messageQueue.size());
      while(!messageQueue.isEmpty()){

        MessageSendToSubscriber m = messageQueue.take();
        byte[] sendData  = new byte[1024];


        // Getting the data from the message queue
        String sentence = m.message;
        InetAddress subscriberIP = m.subscriberIP;
        int subPort = m.subPort;

        String capitalizedSentence = sentence.toUpperCase();
        sendData = capitalizedSentence.getBytes();

        DatagramPacket sendPacket =
          new DatagramPacket(sendData, sendData.length, subscriberIP, subPort);

        serverSocket.send(sendPacket);
      }
      
    }
    catch(IOException e){
      System.err.println(e);
    }
    catch(InterruptedException e){
      e.printStackTrace();
    }
  }
}

// INSERT INTO sub_info (pub_ip,pub_port,sub_ip,sub_port) VALUES ("172.21.4.237", 6566, "172.21.4.237", 6703);
// SELECT DISTINCT  FROM sub_info WHERE pub_ip =  publisherIP AND pub_port =;