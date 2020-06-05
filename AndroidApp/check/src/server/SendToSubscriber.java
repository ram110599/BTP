/**
 * 
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

import message.Message;
import message.MessageSendToSubscriber;

/**
 * @author abhi
 *
 */
public class SendToSubscriber implements Runnable {
	private BlockingQueue <MessageSendToSubscriber> messageQueue;
	DatagramSocket serverSocket;

	/**
	 * @param messageQueue
	 * @param serverSocket
	 */
	public SendToSubscriber(
			BlockingQueue<MessageSendToSubscriber> messageQueue,
			DatagramSocket serverSocket) {
		this.messageQueue = messageQueue;
		this.serverSocket = serverSocket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// TODO Auto-generated method stub
		sendPacketToSubscriber();
        System.out.println("Current Thread " + Thread.currentThread().getName());

	}
	
	private void sendPacketToSubscriber(){
		System.out.println("In consumer thread.");
	    System.out.println("Size of the messageQueue is " + messageQueue.size());
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
