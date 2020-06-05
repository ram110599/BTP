/**
 * 
 */
package publisher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import message.Message;

/**
 * @author abhi
 *
 */
public class Publisher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Taking input from the user		
		BufferedReader inFromUser =
		        new BufferedReader(new InputStreamReader(System.in));
		
		// Port of the client
	    System.out.println("Enter the port number of the client.");
	    int port;
	    try {
			port = Integer.parseInt(inFromUser.readLine());
			DatagramSocket clientSocket = new DatagramSocket(port);
			int serverPort = 9876;
			String serverIpInString = "172.21.4.237";
			
		    InetAddress serverIP = InetAddress.getByName(serverIpInString);
		    
			// Iterating all the  time
		    while(true){
		        // clientSocket.setSoTimeout(10000);
		        byte[] sendData = new byte[1024];
		        byte[] receiveData = new byte[1024];
	
		        System.out.println("Enter bye if you want to terminate.");
		        String sentence = inFromUser.readLine();
//		        System.out.println(sentence);
	
		        // checking the termination condition
		        if(sentence.equalsIgnoreCase("bye")){
		          break;
		        }
		        
		        Message m = new Message(sentence);
//		        System.out.println("Message in class is " + m.getMessage());
		        sendData = m.getBytes();
	
		        DatagramPacket sendPacket =
		           new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
	
		        clientSocket.send(sendPacket);
		        System.out.println("Message Sent To Server");
		        DatagramPacket receivePacket =
		           new DatagramPacket(receiveData, receiveData.length);
	
		        clientSocket.receive(receivePacket);
		        System.out.println("Message Received from the server.");
		        
		        byte[] data = receivePacket.getData();
		        System.out.println("Data length is " + data.length);
		        
		        Message messageFromServer = Message.getMessageFromByte(data);
	
		        String modifiedSentence = messageFromServer.getMessage();
	
		        System.out.println("FROM SERVER:" + modifiedSentence);
	
		      }
		      
		      clientSocket.close();
		
	    } catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
