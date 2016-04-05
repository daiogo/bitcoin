/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import static bitcoin.Peer.GROUP_IP;
import static bitcoin.Peer.MULTICAST_PORT;
import bitcoin.messages.ExitMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.security.PublicKey;
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
    
    public static byte[] serialize_object(Object object){
        byte[] serialized_object = null;
        try {
            ObjectOutputStream objectOut = null;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            serialized_object = byteOut.toByteArray();
            return serialized_object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serialized_object;
    }   
    
    public void sendHello(UserInformation userInformation)throws IOException {        
        byte[] messageBytes = serialize_object(userInformation);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendDatabase(Database database, int unicastPort)throws IOException {        
        byte[] messageBytes = serialize_object(database);
        UDPClient udpClient = new UDPClient(unicastPort);
        udpClient.sendDatabase(messageBytes);
    }
    
    public void sendExit(UserInformation userInformation)throws IOException {   
        ExitMessage exitMessage = new ExitMessage(userInformation);
        byte[] messageBytes = serialize_object(exitMessage);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendTransaction() throws IOException {
        byte[] m = "OLA BOCOS".getBytes();
        outPacket = new DatagramPacket(m, m.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }

}
