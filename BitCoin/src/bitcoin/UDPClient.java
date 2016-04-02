package bitcoin;

import java.net.*;
import java.io.*;
public class UDPClient{
    
    public void send_message(String message, String hostname){ 
        // args give message contents and destination hostname
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();    
            byte [] m = message.getBytes();
            InetAddress aHost = InetAddress.getByName(hostname);
            int serverPort = 6789;		                                                 
            DatagramPacket request =
                    new DatagramPacket(m, message.length(), aHost, serverPort);
            aSocket.send(request);			                        
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
            aSocket.receive(reply);
            System.out.println("Reply: " + new String(reply.getData()));	
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }		      	
}
