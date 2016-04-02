/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import static bitcoin.Peer.GROUP_IP;
import static bitcoin.Peer.MULTICAST_PORT;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
Multicast Messages:
hello = hello,username,coinPrice,port,publicKey
mining = mining,username,transaction,valid(true/false)
byebye = byebye,username

Unicast Messages:
buy = buy,username
database = database
*/

public class MessageSender {
    private DatagramPacket outPacket;
    private InetAddress group;
    private MulticastSocket socket;
        
    public MessageSender(MulticastSocket socket) throws UnknownHostException {
        this.socket = socket;
        this.group = InetAddress.getByName(GROUP_IP);
    }
    
    public void sendHello(String username, String coinPrice, int unicast_port, byte[] encodedPublicKey) throws IOException {
        String message = "hello," + username + "," + coinPrice + ","
                + Integer.toString(unicast_port) + "," + Arrays.toString(encodedPublicKey);
        byte[] messageBytes = message.getBytes();
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendTransaction() throws IOException {
        byte[] m = "OLA BOCOS".getBytes();
        outPacket = new DatagramPacket(m, m.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    /*
    @Override
    public void run() {
        // Parse message
        // Define protocol like 
        // Eg. hello|public key|coins|...
        //messageType = message.matches("^[a-z]+|");
        String messageType = "hello";
        
        try {
            switch (messageType) {
                case "hello":
                    sendHello(message.getBytes());
                    break;
                case "trasaction":
                    sendTransaction(message.getBytes());
                    break;
                default:
                    System.out.println("ERROR | Message to be sent doesn't follow messaging protocol");
                    break;
            }
<<<<<<< HEAD
            
=======

            //socket.close();
>>>>>>> a4fdb5b2b839ea6e85a88c92ad9607bf0968d255
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
}
