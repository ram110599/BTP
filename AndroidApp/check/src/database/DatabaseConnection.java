/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * @author abhi
 *
 */
public class DatabaseConnection {

	/**
	 * 
	 */
	private String url;
	private String user;
	private String password;
	private Connection conn;
	
	
	public DatabaseConnection() {
		this.url = null;
		this.user = null;
		this.password = null;
	}


	/**
	 * @param url
	 * @param user
	 * @param password
	 */
	public DatabaseConnection(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
		
		//	Setting the Connection object	
		try {
			this.conn = getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	
	/**
	 * @param url
	 * @param user
	 * @param password
	 * @return Connection object
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception{
		try{
			Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
			System.out.println("Connection Established with database.");
			return conn;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param publisherIP
	 * @param pubPort
	 * @return resultSet
	 */
	public ResultSet getSubscriberInfo(String publisherIP, int pubPort){
		String query = "SELECT DISTINCT sub_port, sub_ip FROM sub_info WHERE pub_ip = '"+ publisherIP + "' AND pub_port = " + pubPort +";";
	    try {
			Statement stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return null;
	}
	
	/**
	 * @param publisherId
	 * @return
	 */
	public ResultSet getSubscriberInfo(String publisherId){
		String query = "SELECT DISTINCT sub_port, sub_ip FROM sub_info WHERE pub_id = '"+ publisherId + "';";
		Statement stmt;
		try {
			stmt = this.conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
