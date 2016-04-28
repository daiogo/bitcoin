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
import bitcoin.UserInformation;
import bitcoin.messages.BuyMessage;
import bitcoin.messages.ExitMessage;
import bitcoin.messages.MiningMessage;
import bitcoin.messages.TransactionMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class MessageHandler extends Thread {
    private static final int REWARD_VALUE = 1;
    private Peer myPeer;
    private byte[] message;
    
    public MessageHandler(byte[] message, Peer peer) {
        myPeer = peer;
        this.message = message;
    }
    
    public static Object deserialize_object(byte[] message) {
        ObjectInputStream objectIn = null;
        Object object = null;
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(message);
            objectIn = new ObjectInputStream(byteIn);
            object = objectIn.readObject();
            return object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objectIn.close();
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
        
        switch(objectName){
            case "bitcoin.UserInformation":
                handleHelloMessage(UserInformation.class.cast(object));
                myPeer.windowAddMessageReceived("Database");
                break;
            case "bitcoin.Database":
                handleDatabaseMessage(Database.class.cast(object));
                myPeer.windowAddMessageReceived("Database");
                break;
            case "bitcoin.messages.ExitMessage":
                handleExitMessage(ExitMessage.class.cast(object));
                myPeer.windowAddMessageReceived("Exit");
                break;
            case "bitcoin.messages.BuyMessage":
                handleBuyMessage(BuyMessage.class.cast(object));
                myPeer.windowAddMessageReceived("Buy");
                break;
            case "bitcoin.messages.TransactionMessage":
                handleTransactionMessage(TransactionMessage.class.cast(object));
                myPeer.windowAddMessageReceived("Transaction");
                break;
            case "bitcoin.messages.MiningMessage":
                handleMiningMessage(MiningMessage.class.cast(object));
                myPeer.windowAddMessageReceived("Mining");
                break;
            default:
                System.out.println("Message received class not found: " + objectName);
                break;
        }
    }
    
    public void handleHelloMessage(UserInformation userInformation) {
        // If message came from myself, ignore it
        if (!userInformation.getUsername().equals(myPeer.getUsername())) {
            System.out.println("Received Hello message");
            
            // Add new user to database
            myPeer.databaseAddUserInformation(userInformation);
            myPeer.sendUnicastMessage("database", userInformation.getUnicastPort(), "", 0);
        }
    }
    
    public synchronized void handleDatabaseMessage(Database database) {
        System.out.println("Received Database message");
        myPeer.setDatabase(database);
    }
    
    public void handleExitMessage(ExitMessage exitMessage){
        System.out.println("Received Exit message");
        myPeer.databaseRemoveUserInformation(exitMessage.getUserInformation());
    }
    
    public void handleBuyMessage(BuyMessage buyMessage){
        System.out.println("Received Buy message");
        System.out.println("User " + buyMessage.getBuyerUsername() + 
                " wants to buy " + buyMessage.getCoins() + " coins " +
                "from: "+buyMessage.getSellerUsername());

        // Sends transaction message  
        myPeer.sendTransactionMessage(buyMessage);
    }

    public void handleTransactionMessage(TransactionMessage transactionMessage) {
        System.out.println("Received Transaction message");
        
        // Starts mining process for this transaction
        Miner miner = new Miner(transactionMessage, myPeer);
        miner.start();
    }

    public void handleMiningMessage(MiningMessage miningMessage) {
        System.out.println("Received Mining message");
        Database database = myPeer.getDatabase();
        for (int i = 0; i < database.getNumberOfTransactions(); i++) {
            MiningMessage temp = (MiningMessage) database.getArrayTransactions().get(i);
            if (temp.getId().equals(miningMessage.getId())) { //&& temp.getTimestamp() > miningMessage.getTimestamp()) {
                //System.out.println("Replacing miner...");
                //System.out.println("Current timestamp is: " + temp.getTimestamp());
                //System.out.println("Next timestamp will be: " + miningMessage.getTimestamp());
                //database.getArrayTransactions().set(i, miningMessage);
                System.out.println("ERROR | Sorry, another peer mined it faster");
                return;
            }
        }
        
        // Adds official miner to the database
        database.getArrayTransactions().add(miningMessage);

        // Gathers info from the transaction
        String buyerUsername = miningMessage.getBuyerUsername();
        String sellerUsername = miningMessage.getSellerUsername();
        String minerUsername = miningMessage.getMinerUsername();
        int ammount = miningMessage.getAmmount();
        
        // Performs credits and debits on accounts
        for (int i = 0; i < database.getNumberOfUsers(); i++) {
            UserInformation temp = (UserInformation) database.getArrayUserInformation().get(i);

            if (temp.getUsername().equals(sellerUsername)) {
                temp.setCoins(temp.getCoins() - ammount - REWARD_VALUE);
            }
            
            if (temp.getUsername().equals(buyerUsername)) {
                temp.setCoins(temp.getCoins() + ammount);
            }
            
            if (temp.getUsername().equals(minerUsername)) {
                temp.setCoins(temp.getCoins() + REWARD_VALUE);
            }
            
            // Updates database with credits and debits performed on this transaction
            database.getArrayUserInformation().set(i, temp);
        }
        
        // Sends updated database to all peers
        try {
            MessageSender sender = new MessageSender(myPeer.getMulticastSocket());
            sender.updateDatabase(database);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MessageHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
