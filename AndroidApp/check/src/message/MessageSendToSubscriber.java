/**
 * 
 */
package message;

import java.net.InetAddress;

/**
 * @author abhi
 *
 */
public class MessageSendToSubscriber {
	private Message message;
	private int pubPort;
	private int subPort;
	private InetAddress publisherIP;
	private InetAddress subscriberIP;
	
	

	/**
	 * @param message
	 * @param pubPort
	 * @param subPort
	 * @param publisherIP
	 * @param subscriberIP
	 */
	public MessageSendToSubscriber(Message message, int pubPort, int subPort,
			InetAddress publisherIP, InetAddress subscriberIP) {
		this.message = message;
		this.pubPort = pubPort;
		this.subPort = subPort;
		this.publisherIP = publisherIP;
		this.subscriberIP = subscriberIP;
	}


	/**
	 * @return the message
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(Message message) {
		this.message = message;
	}

	
	/**
	 * @return the pubPort
	 */
	public int getPubPort() {
		return pubPort;
	}

	/**
	 * @param pubPort the pubPort to set
	 */
	public void setPubPort(int pubPort) {
		this.pubPort = pubPort;
	}

	/**
	 * @return the subPort
	 */
	public int getSubPort() {
		return subPort;
	}

	/**
	 * @param subPort the subPort to set
	 */
	public void setSubPort(int subPort) {
		this.subPort = subPort;
	}

	/**
	 * @return the publisherIP
	 */
	public InetAddress getPublisherIP() {
		return publisherIP;
	}

	/**
	 * @param publisherIP the publisherIP to set
	 */
	public void setPublisherIP(InetAddress publisherIP) {
		this.publisherIP = publisherIP;
	}

	/**
	 * @return the subscriberIP
	 */
	public InetAddress getSubscriberIP() {
		return subscriberIP;
	}

	/**
	 * @param subscriberIP the subscriberIP to set
	 */
	public void setSubscriberIP(InetAddress subscriberIP) {
		this.subscriberIP = subscriberIP;
	}
	

}
