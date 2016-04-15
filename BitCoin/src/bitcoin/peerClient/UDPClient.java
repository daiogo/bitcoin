package bitcoin.peerClient;

import java.net.*;
import java.io.*;

public class UDPClient{
    public static final int MAX_UDP_MESSAGE_SIZE = 65535;
    private int unicastPort;
    
    public UDPClient(int unicastPort){
        this.unicastPort = unicastPort;
    }
    
    
    public void sendDatabase(byte[] byteDatabase) { 
        DatagramSocket clientSocket = null;
        
        try {
            clientSocket = new DatagramSocket();    
            InetAddress clientHost = InetAddress.getByName("localhost");
            int serverPort = unicastPort;		                                                 
            DatagramPacket request =
                    new DatagramPacket(byteDatabase, byteDatabase.length, clientHost, serverPort);
            clientSocket.send(request);
 
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(clientSocket != null) clientSocket.close();}
    }	
    
    public void sendBuy(byte[] buyMessage) {  
        DatagramSocket clientSocket = null;
        
        try {
            clientSocket = new DatagramSocket();    
            InetAddress clientHost = InetAddress.getByName("localhost");
            int serverPort = unicastPort;		                                                 
            DatagramPacket request =
                    new DatagramPacket(buyMessage, buyMessage.length, clientHost, serverPort);
            clientSocket.send(request);
            
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(clientSocket != null) clientSocket.close();}
    }
}
