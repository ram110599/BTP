/**
 * 
 */
package server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import database.DatabaseConnection;

import message.Message;
import message.MessageSendToSubscriber;

/**
 * @author abhi
 *
 */
public class SubscriberKeeper implements Runnable {
	private BlockingQueue<MessageSendToSubscriber> messageQueue;
	private DatabaseConnection d;
	private String publisherIP;
	private int pubPort;
	private Message message;
	
	
	/**
	 * @param messageQueue
	 * @param d
	 * @param publisherIP
	 * @param pubPort
	 * @param message
	 */
	public SubscriberKeeper(
			BlockingQueue<MessageSendToSubscriber> messageQueue,
			DatabaseConnection d, String publisherIP, int pubPort,
			Message message) {
		this.messageQueue = messageQueue;
		this.d = d;
		this.publisherIP = publisherIP;
		this.pubPort = pubPort;
		this.message = message;
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * Overriding the run
	 */
	public void run() {
		// TODO Auto-generated method stub
		// Adding subscriber in the queue
        addingSubscriber();
	}
	
	/**
	 * For adding subscriber in the message queue
	 */
	private void addingSubscriber(){
		ResultSet rs = d.getSubscriberInfo(this.publisherIP, this.pubPort);
		try {
			while(rs.next()){
				
				// Subscriber Port and IP
		        int subPort = rs.getInt("sub_port");
		        String subscriberIP = rs.getString("sub_ip");
		        
		        
		        // Adding messageSendTosubscriber in queue
		        try {
		        	InetAddress pubIP = InetAddress.getByName(publisherIP);
			        InetAddress subIP = InetAddress.getByName(subscriberIP);
					MessageSendToSubscriber m = new MessageSendToSubscriber(message, pubPort, subPort, pubIP , subIP );
					messageQueue.put(m);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			System.out.println("The size of messageQueue is " + messageQueue.size() + " in Producer thread.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
