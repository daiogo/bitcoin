package bitcoin.peerServer;

import bitcoin.Peer;
import java.net.*;
import java.io.*;

public class UDPServer extends Thread{
    
    public static final int MAX_UDP_MESSAGE_SIZE = 65535;
    private Peer myPeer;
    private int unicastPort;
    
    public UDPServer(int unicastPort, Peer peer){
        myPeer = peer;
        this.unicastPort = unicastPort;
    }
    
    public void run(){ 
        DatagramSocket serverSocket = null;

        System.out.println("UDP Server Started on port " + unicastPort);
        try{
            serverSocket = new DatagramSocket(unicastPort);
            // Create socket at given port
            byte[] buffer = new byte[MAX_UDP_MESSAGE_SIZE];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(request);
                DatagramPacket inPacket = new DatagramPacket(request.getData(), request.getLength(), 
                    request.getAddress(), request.getPort());
                
                // Starts handler thread to handle message properly
                MessageHandler handler = new MessageHandler(inPacket.getData(), myPeer);
                handler.start();
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(serverSocket != null) serverSocket.close();}
    }
}
