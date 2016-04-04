package bitcoin;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class UDPClient{
    public static final int MAX_UDP_MESSAGE_SIZE = 65535;
    private int unicastPort;
    
    public UDPClient(int unicastPort){
        this.unicastPort = unicastPort;
    }
    
    
    public void sendDatabase(byte[] byteDatabase){ 
        // args give message contents and destination hostname
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();    
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = unicastPort;		                                                 
            DatagramPacket request =
                    new DatagramPacket(byteDatabase, byteDatabase.length, aHost, serverPort);
            aSocket.send(request);		
            System.out.println("Database Message Sent to "+ unicastPort);
	
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }		      	
}
