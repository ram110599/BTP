package subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

import message.Message;

public class Subscriber {

	private static DatagramSocket clientSocket;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BufferedReader inFromUser =
		        new BufferedReader(new InputStreamReader(System.in));

		      // Port of the client
		      System.out.println("Enter the port number of the client.");
		      int port;

		      try {
		    	port = Integer.parseInt(inFromUser.readLine());  
				clientSocket = new DatagramSocket(port);
				
				while(true){
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket =
					           new DatagramPacket(receiveData, receiveData.length);

					clientSocket.receive(receivePacket);

					byte[] data = receivePacket.getData();
			        Message messageFromServer = Message.getMessageFromByte(data);
		
			        String modifiedSentence = messageFromServer.getMessage();
		
			        System.out.println("FROM SERVER:" + modifiedSentence);
				}
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      

	}

}
