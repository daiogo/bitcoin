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
        DatagramSocket aSocket = null;

        System.out.println("UDP Server Started on port " + unicastPort);
        try{
            aSocket = new DatagramSocket(unicastPort);
            // create socket at agreed port
            byte[] buffer = new byte[MAX_UDP_MESSAGE_SIZE];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);     
                DatagramPacket inPacket = new DatagramPacket(request.getData(), request.getLength(), 
                    request.getAddress(), request.getPort());
                //System.out.println("Unicast Message Received");
                MessageHandler handler = new MessageHandler(inPacket.getData(),myPeer);
                handler.run();
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
}
