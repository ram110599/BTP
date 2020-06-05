/**
 * 
 */
package message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author abhi
 *
 */
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String message;
	int type;
	/**
	 * @param message
	 * @param type
	 */
	public Message(String message, int type) {
		this.message = message;
		this.type = type;
	}
	/**
	 * @param message
	 */
	public Message(String message) {
		this.message = message;
		this.type = 1;
	}
	public Message() {
		// TODO Auto-generated constructor stub
		message = null;
		type = 1;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * @return bytes array 
	 * convert message into byte array
	 */
	public byte[] getBytes(){
		Message m = Message.this;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(outputStream);
			os.writeObject(m);
			byte[] data = outputStream.toByteArray();
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * @param data it is bytes array
	 * @return Message Object
	 * Converts byte data into Message Object
	 */
	public static Message getMessageFromByte(byte[] data){
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		try {
			ObjectInputStream is = new ObjectInputStream(in);
			Message message = new Message(); 
			message = (Message) is.readObject();
			return message;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
