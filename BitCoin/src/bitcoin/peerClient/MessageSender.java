/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin.peerClient;

// bitcoin package
import static bitcoin.Peer.GROUP_IP;
import static bitcoin.Peer.MULTICAST_PORT;
import bitcoin.Database;
import bitcoin.ObjectSerializer;
import bitcoin.UserInformation;
import bitcoin.Wallet;

// peerClient package
import bitcoin.peerClient.UDPClient;

// messages package
import bitcoin.messages.BuyMessage;
import bitcoin.messages.ExitMessage;
import bitcoin.messages.MiningMessage;
import bitcoin.messages.TransactionMessage;

// java imports
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender {
    private DatagramPacket outPacket;
    private InetAddress group;
    private MulticastSocket socket;
    
    public MessageSender(MulticastSocket socket) throws UnknownHostException {
        this.socket = socket;
        this.group = InetAddress.getByName(GROUP_IP);   
    }
    
    public void sendHello(UserInformation userInformation) throws IOException {      
        System.out.println("Sending Hello Message");
        byte[] messageBytes = ObjectSerializer.serialize_object(userInformation);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendDatabase(Database database, int unicastPort) throws IOException {        
        byte[] messageBytes = ObjectSerializer.serialize_object(database);
        UDPClient udpClient = new UDPClient(unicastPort);
        udpClient.sendDatabase(messageBytes);
    }
    
    public void sendExit(UserInformation userInformation) throws IOException {   
        ExitMessage exitMessage = new ExitMessage(userInformation);
        byte[] messageBytes = ObjectSerializer.serialize_object(exitMessage);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    
    public void sendBuy(BuyMessage buyMessage, int unicastPort) throws IOException {
        byte[] messageBytes = ObjectSerializer.serialize_object(buyMessage);
        UDPClient udpClient = new UDPClient(unicastPort);
        udpClient.sendBuy(messageBytes);
    }
    
    public void sendTransaction(BuyMessage buyMessage, Wallet wallet) throws IOException {
        byte[] serializedBuyMessage = ObjectSerializer.serialize_object(buyMessage);
        byte[] signedMessage = wallet.signFile(serializedBuyMessage);
        String uniqueId = UUID.randomUUID().toString();
        TransactionMessage transactionMessage = new TransactionMessage(uniqueId, serializedBuyMessage, signedMessage);
        byte[] messageBytes = ObjectSerializer.serialize_object(transactionMessage);
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }

    public void updateDatabase(Database database) throws IOException {
        byte[] messageBytes = ObjectSerializer.serialize_object(database);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
    public void sendMining(MiningMessage miningMessage) throws IOException {        
        byte[] messageBytes = ObjectSerializer.serialize_object(miningMessage);
        
        outPacket = new DatagramPacket(messageBytes, messageBytes.length, group, MULTICAST_PORT);
        socket.send(outPacket);
    }
    
}
