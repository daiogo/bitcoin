/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin.peerServer;

import bitcoin.Database;
import bitcoin.peerClient.MessageSender;
import bitcoin.Miner;
import bitcoin.Peer;
import bitcoin.SignatureVerifier;
import bitcoin.UserInformation;
import bitcoin.messages.BuyMessage;
import bitcoin.messages.ExitMessage;
import bitcoin.messages.TransactionMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class MessageHandler extends Thread{ 
    private Peer myPeer;
    private byte[] message;
    private SignatureVerifier signatureVerifier = new SignatureVerifier();
    
    public MessageHandler(byte[] message, Peer peer) {
        myPeer = peer;
        this.message = message;
    }
    
    public static Object deserialize_object(byte[] message) {
        ObjectInputStream objIn = null;
        Object object = null;
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(message);
            objIn = new ObjectInputStream(byteIn);
            object = objIn.readObject();
            return object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objIn.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return object;
    }
    
    @Override
    public void run () {
        Object object = deserialize_object(message);
        String objectName = object.getClass().getName();
        //System.out.println("Class name: " + objectName);
        
        switch(objectName){
            case "bitcoin.UserInformation":
                //Hello message
                handle_hello_message(UserInformation.class.cast(object));
                break;
            case "bitcoin.Database":
                handle_database_message(Database.class.cast(object));
                break;
            case "bitcoin.messages.ExitMessage":
                handle_exit_message(ExitMessage.class.cast(object));
                break;
            case "bitcoin.messages.BuyMessage":
                handleBuyMessage(BuyMessage.class.cast(object));
                break;
            case "bitcoin.messages.TransactionMessage":
                handleTransactionMessage(TransactionMessage.class.cast(object));
                break;
            default:
                System.out.println("Message received class not found: " + objectName);
                break;
        }
    }
    
    public void handle_hello_message(UserInformation userInformation){
        //ignore my own message
        if(!userInformation.getUsername().equals(myPeer.getUsername())){
            System.out.println("Received Hello Message");
            //add new user to database
            myPeer.databaseAddUserInformation(userInformation);
            myPeer.sendUnicastMessage("database", userInformation.getUnicastPort(), "", 0);
        }
    }
    
    public synchronized void handle_database_message(Database database){
        System.out.println("Received Database message");
        myPeer.setDatabase(database);
    }
    
    public void handle_exit_message(ExitMessage exitMessage){
        System.out.println("Received Exit Message");
        myPeer.databaseRemoveUserInformation(exitMessage.getUserInformation());
    }
    
    public void handleBuyMessage(BuyMessage buyMessage){
        System.out.println("Received Buy Message");
        System.out.println("User " + buyMessage.getBuyerUsername() + 
                " wants to buy " + buyMessage.getCoins() + " coins." +
                "from: "+buyMessage.getSellerUsername());
        myPeer.sendTransactionMessage(buyMessage);
        // Create transaction message  
    }
    
    public void handleTransactionMessage(TransactionMessage transactionMessage){
        System.out.println("Received Transaction Message");
        //call Mining method
        Miner miner = new Miner(transactionMessage, myPeer);
        miner.start();
    }
    
    
}
