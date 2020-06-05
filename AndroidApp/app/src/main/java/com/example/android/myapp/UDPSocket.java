package com.example.android.myapp;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UDPSocket {
    private int LocalPort = 80;
    private final Handler mHandler;
    private UDPThread SocketRecv;

    UDPSocket(Handler handler, int localport){
        LocalPort = localport;
        mHandler = handler;
        if(localport <= 1024){
            Log.e("UDP", "UDPSocket: Port number is less than 1024 ");
        }
    }

    public void startRecv(){      // Listening port
        SocketRecv = new UDPThread();
        SocketRecv.setRecv_Flag(true);
        SocketRecv.start();
    }

    public void Send(String message,String address,int port){
        SocketRecv.setRecv_Flag(false);
        UDPThread SocketSend = new UDPThread();
        SocketSend.write(message,address,port);
        SocketSend.start();
    }

    private class UDPThread extends Thread{
        private DatagramSocket mm_socket;
        private byte[] mm_data;
        private boolean Send_Flag = false;
        private boolean Recv_Flag = false;
        private String des_Address;
        private int desPort;

        UDPThread( ){   // Local port
            if(mm_socket == null){
                try {
                    mm_socket = new DatagramSocket(null);
                    mm_socket.setReuseAddress(true);
                    mm_socket.bind(new InetSocketAddress(LocalPort));
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override public void run() {
            if(Send_Flag){
                SendDataToServer();
                Send_Flag = false;
            }
            while (Recv_Flag) {
                ReceiveServerSocketData();
            }
        }

        private   void setRecv_Flag(boolean recv_Flag){
            Recv_Flag = recv_Flag;
        }

        private void write(String command,String address,int port){
            des_Address = address;
            desPort     = port;
            mm_data     = command.getBytes();
            Send_Flag   = true;
        }

        private void SendDataToServer() {
            try{
                InetAddress serverAddress = InetAddress.getByName(des_Address);
                DatagramPacket packet = new DatagramPacket(mm_data,mm_data.length,
                        serverAddress,desPort);
                mm_socket.send(packet);// Send data to the server。
                mHandler.obtainMessage(MainActivity.MESSAGE_WRITE,packet.getLength(),
                        -1,packet.getData()).sendToTarget();
            }catch(SocketException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }

        private void ReceiveServerSocketData() {
            try {
                //The instantiated port number must be the same as the socket when sending, otherwise no data will be received
                byte data[]=new byte[4*1024];
                //Parameter 1: data to be accepted Parameter 2: length of data
                DatagramPacket packet = new DatagramPacket(data,data.length);
                mm_socket.receive(packet);
                mHandler.obtainMessage(MainActivity.MESSAGE_READ,packet.getLength(),
                        -1,packet.getData()).sendToTarget();
            }catch(SocketException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // Communication using TCP protocol
    protected void connectServerWithTCPSocket(String msg,String addrServer,int localPort) {
        Socket socket;
        try {
            socket = new Socket(addrServer, localPort);
            // Get the Socket's OutputStream object for sending data.
            OutputStream outputStream = socket.getOutputStream();
            byte buffer[] = msg.getBytes();
            // Send the read data to the server
            outputStream.flush();
        }  catch (UnknownHostException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // TCP
    //server
    public void ServerReceviedByTcp() {
        // Declare a ServerSocket object
        ServerSocket serverSocket = null;
        try{
            //Create a ServerSocket object and let this Socket listen on port 1989
            serverSocket = new ServerSocket(1989);
            // Call the accept () method of ServerSocket to accept the request sent by the client，
            // If the client is not sending data, the thread is stalled and does not continue
            Socket socket = serverSocket.accept();
            // Get InputStream object from Socket
            InputStream inputStream = socket.getInputStream();
            byte buffer[] = new byte[1024 * 4];
            int temp = 0;
            // Read the data sent by the client from the InputStream
            while ((temp = inputStream.read(buffer)) != -1) {
                System.out.println(new String(buffer, 0, temp));
            }
            serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
