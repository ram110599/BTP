import java.io.*;
import java.net.*;

class Subscriber {
    public static void main(String args[]) throws Exception
    {

      BufferedReader inFromUser =
        new BufferedReader(new InputStreamReader(System.in));

      // Port of the client
      System.out.println("Enter the port number of the client.");
      int port = Integer.parseInt(inFromUser.readLine());

      DatagramSocket clientSocket = new DatagramSocket(port);
      InetAddress IPAddress = InetAddress.getLocalHost();
      // InetAddress IPAddress = InetAddress.getByName("hostname");

      // Iterating all the  time
      while(true){
        // byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        // System.out.println("Enter bye if you want to terminate.");
        // String sentence = inFromUser.readLine();

        // // checking the termination condition
        // if(sentence.equalsIgnoreCase("bye")){
        //   break;
        // }

        // sendData = sentence.getBytes();

        // DatagramPacket sendPacket =
        //    new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        // clientSocket.send(sendPacket);

        DatagramPacket receivePacket =
           new DatagramPacket(receiveData, receiveData.length);

        clientSocket.receive(receivePacket);

        String modifiedSentence =
            new String(receivePacket.getData());

        System.out.println("FROM SERVER:" + modifiedSentence);

      }
      
    }
}
