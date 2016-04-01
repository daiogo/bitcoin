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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class MessageSender extends Thread {
    private DatagramPacket outPacket;
    private InetAddress group;
    private MulticastSocket socket;
    private String message;
    private String messageType;
        
    public MessageSender(MulticastSocket socket, String message) throws UnknownHostException {
        this.socket = socket;
        this.group = InetAddress.getByName(GROUP_IP);
        this.message = message;
    }
    
    public void sendHello(byte[] helloMessage) throws IOException {
        outPacket = new DatagramPacket(helloMessage, helloMessage.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendTransaction(byte[] transactionMessage) throws IOException {
        outPacket = new DatagramPacket(transactionMessage, transactionMessage.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    @Override
    public void run() {
        // Parse message
        // Define protocol like 
        // Eg. hello|public key|coins|...
        //messageType = message.matches("^[a-z]+|");
        messageType = "hello";
        
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
            socket.leaveGroup(group);
            //socket.close();
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
