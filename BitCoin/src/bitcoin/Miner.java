/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import bitcoin.peerClient.MessageSender;
import static bitcoin.peerServer.MessageHandler.deserialize_object;
import bitcoin.messages.BuyMessage;
import bitcoin.messages.MiningMessage;
import bitcoin.messages.TransactionMessage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Random;
import java.util.UUID;
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
        
        // Neither seller nor buyer are permitted to mine the trasaction
        BuyMessage buyMessage = (BuyMessage) deserialize_object(serializedBuyMessage);
        String buyerUsername = buyMessage.getBuyerUsername();
        String sellerUsername = buyMessage.getSellerUsername();
        int ammount = buyMessage.getCoins();
        
        // If I'm the buyer or seller, don't try to mine
        if (myPeer.getUsername().equals(sellerUsername) || myPeer.getUsername().equals(buyerUsername)) {
            return;
        }

        // Sleeps thread from 3 to 10 seconds randomly to simulate mine processing time
        try {
            Thread.sleep(randomGenerator.nextInt(7000) + 3000);
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
        }

        System.out.println("ERROR | Signature verified!");
        // Update database coins
        Database database = myPeer.getDatabase();

        for (int i = 0; i < database.getNumberOfUsers(); i++) {
            UserInformation temp = (UserInformation) database.getArrayUserInformation().get(i);

            if (temp.getUsername().equals(sellerUsername) && temp.getCoins() < ammount + REWARD_VALUE) {
                System.out.println("ERROR | Seller does not have enough coins, transaction cancelled");
                return;
            }
        }

        // Send mining details to all users
        try {
            MiningMessage miningMessage = new MiningMessage(transactionMessage.getId(), ammount, sellerUsername, buyerUsername, myPeer.getUsername(), System.currentTimeMillis());
            MessageSender sender = new MessageSender(myPeer.getMulticastSocket());
            sender.sendMining(miningMessage);
            System.out.println("Just sent mining message with the id " + transactionMessage.getId());
        } catch (UnknownHostException ex) {
            Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Miner.class.getName()).log(Level.SEVERE, null, ex);
        }            

    }
}
