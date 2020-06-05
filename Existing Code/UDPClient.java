import java.io.*;
import java.net.*;

// Publisher of the 
class UDPClient {
    public static void main(String args[]) throws Exception
    {

      BufferedReader inFromUser =
        new BufferedReader(new InputStreamReader(System.in));

      // Port of the client
      System.out.println("Enter the port number of the client.");
      int port = Integer.parseInt(inFromUser.readLine());

      DatagramSocket clientSocket = new DatagramSocket(port);
      int serverPort = 9876;
      InetAddress serverIPAddress = InetAddress.getByName("172.21.4.237");
      // InetAddress serverIPAddress = InetAddress.getLocalHost();
      // InetAddress serverIPAddress = InetAddress.getByName("hostname");

      // Iterating all the  time
      while(true){
        // clientSocket.setSoTimeout(10000);
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        System.out.println("Enter bye if you want to terminate.");
        String sentence = inFromUser.readLine();

        // checking the termination condition
        if(sentence.equalsIgnoreCase("bye")){
          break;
        }

        sendData = sentence.getBytes();

        DatagramPacket sendPacket =
           new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort);

        clientSocket.send(sendPacket);

        DatagramPacket receivePacket =
           new DatagramPacket(receiveData, receiveData.length);

        clientSocket.receive(receivePacket);

        String modifiedSentence =
            new String(receivePacket.getData());

        System.out.println("FROM SERVER:" + modifiedSentence);

      }
      
      clientSocket.close();

    }
}
