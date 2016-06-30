/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import bitcoin.peerClient.MessageSender;
import bitcoin.messages.BuyMessage;
import bitcoin.messages.MiningMessage;
import bitcoin.messages.TransactionMessage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class Miner extends Thread {
    private static final int REWARD_VALUE = 1;
    private static final int MIN_SLEEP_TIME = 3000;
    private static final int SLEEP_TIME_RANGE = 7000;
    
    private TransactionMessage transactionMessage;
    private Peer myPeer;
    private Random randomGenerator;
    
    public Miner(TransactionMessage transactionMessage, Peer peer) {
        this.transactionMessage = transactionMessage;
        this.randomGenerator = new Random();
        this.myPeer = peer;
    }
    
    public boolean verifySignature(PublicKey publicKey, byte[] signedMessage, byte[] originalMessage){
        // Verifies a DSA signature
        if (signedMessage == null) {
            System.out.println("ERROR | SignatureVerifier | Signed message is null");
            return false;
        }
        try {
            // Create a Signature object and initialize it with the public key
            Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
            signature.initVerify(publicKey);
            
            // Updates original message into signature
            signature.update(originalMessage, 0, originalMessage.length);
            
            // Returns boolean result if original matches the signed message
            return signature.verify(signedMessage);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return false;   // Default return statement
    }
    
    @Override
    public void run() {
        byte[] encryptedBuyMessage = transactionMessage.getEncryptedBuyMessage();
        byte[] serializedBuyMessage = transactionMessage.getSerializedBuyMessage();
        
        BuyMessage buyMessage = (BuyMessage) ObjectSerializer.deserialize_object(serializedBuyMessage);
        String buyerUsername = buyMessage.getBuyerUsername();
        String sellerUsername = buyMessage.getSellerUsername();
        int ammount = buyMessage.getCoins();
        
        // Neither seller nor buyer are permitted to mine the trasaction
        if (myPeer.getUsername().equals(sellerUsername) || myPeer.getUsername().equals(buyerUsername)) {
            return;
        }

        // Sleeps thread from MIN_SLEEP_TIME (ms) + SLEEP_TIME_RANGE (ms) to simulate mine processing time
        try {
            Thread.sleep(randomGenerator.nextInt(SLEEP_TIME_RANGE) + MIN_SLEEP_TIME);
        } catch (InterruptedException ex) {
            Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Buyer can't be the same as seller
        if (buyerUsername.equals(sellerUsername)){
            System.out.println("ERROR | Seller can't be the same as Buyer, transaction cancelled");
            return;
        }
        
        // Verifies signature
        boolean signatureMatches = verifySignature(myPeer.getDatabase().getPublicKey(sellerUsername), encryptedBuyMessage, serializedBuyMessage);
        
        if (!signatureMatches) {
            System.out.println("ERROR | Signature doesn't match, transaction cancelled");
            return;
        } else {
            System.out.println("SUCCESS | Signature verified!");
            
            // Update database coins
            Database database = myPeer.getDatabase();
            
            for (int i = 0; i < database.getNumberOfUsers(); i++) {
                UserInformation temp = (UserInformation) database.getArrayUserInformation().get(i);
                
                // Checks if seller has sufficient funds to perform the transaction
                if (temp.getUsername().equals(sellerUsername) && temp.getCoins() < ammount + REWARD_VALUE) {
                    System.out.println("ERROR | This transaction is not valid");
                    return;
                }
            }
            
            // Send mining details to all users
            try {
                MiningMessage miningMessage = new MiningMessage(transactionMessage.getId(), ammount, sellerUsername, buyerUsername, myPeer.getUsername(), System.currentTimeMillis());
                MessageSender sender = new MessageSender(myPeer.getMulticastSocket());
                sender.sendMining(miningMessage);
                //System.out.println("Just sent mining message with the id " + transactionMessage.getId());
            } catch (UnknownHostException ex) {
                Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
}
