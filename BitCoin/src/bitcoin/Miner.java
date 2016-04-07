/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import static bitcoin.MessageHandler.deserialize_object;
import bitcoin.messages.BuyMessage;
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
    private TransactionMessage transactionMessage;
    private Peer myPeer;
    private Random randomGenerator;
    
    public Miner(TransactionMessage transactionMessage, Peer peer) {
        this.transactionMessage = transactionMessage;
        this.randomGenerator = new Random();
        this.myPeer = peer;
    }
    
    public boolean verifySignature(PublicKey pubKey, byte[] signedMessage, byte[] originalMessage){
        /* Verify a DSA signature */
        if (signedMessage == null) {
            System.out.println("ERROR | SignatureVerifier | Signed message is null");
            return false;
        }
        try {
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);

            sig.update(originalMessage, 0, originalMessage.length);
            
            return sig.verify(signedMessage);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return false;   // Default return statement
    }
    
    @Override
    public void run() {
        byte[] encryptedBuyMessage = transactionMessage.getEncryptedBuyMessage();
        byte[] serializedBuyMessage = transactionMessage.getSerializedBuyMessage();
        BuyMessage buyMessage = (BuyMessage) deserialize_object(transactionMessage.getSerializedBuyMessage());
        String buyerUsername = buyMessage.getBuyerUsername();
        String sellerUsername = buyMessage.getSellerUsername();
        int ammount = buyMessage.getCoins();
        
        if (myPeer.getUsername().equals(sellerUsername) || myPeer.getUsername().equals(buyerUsername)) {
            return;
        }

        // Sleeps thread from 0 to 9 seconds randomly
        try {
            Thread.sleep(randomGenerator.nextInt(7000) + 3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Verifies signature
        boolean signatureMatches = verifySignature(myPeer.getDatabase().getPublicKey(sellerUsername), encryptedBuyMessage, serializedBuyMessage);
        
        if (signatureMatches) {
            System.out.println("Signature verified!");
            
            // Update database coins
            Database database = myPeer.getDatabase();
            
            for (int i = 0; i < database.getNumberOfUsers(); i++) {
                UserInformation temp = (UserInformation) database.getArrayUserInformation().get(i);
                
                if (temp.getUsername().equals(sellerUsername)) {
                    temp.setCoins(temp.getCoins() - ammount - REWARD_VALUE);
                }
                
                if (temp.getUsername().equals(buyerUsername)) {
                    temp.setCoins(temp.getCoins() + ammount);
                }
                
                if (temp.getUsername().equals(myPeer.getUsername())) {
                    temp.setCoins(temp.getCoins() + REWARD_VALUE);
                }
            }
            
            try {
                // Send database to users
                MessageSender sender = new MessageSender(myPeer.getMulticastSocket());
                sender.updateDatabase(database);
            } catch (UnknownHostException ex) {
                Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            System.out.println("ERROR | Signature doesn't match");
        }
    }
}
