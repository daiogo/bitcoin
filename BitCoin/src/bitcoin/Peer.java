/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.net.*;
import java.io.*;
import java.security.*;

import javax.crypto.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author diego
 */
public class Peer {
    public static final int MIN_USERS = 4;
    public static final int MULTICAST_PORT = 6789;
    public static final String GROUP_IP = "228.5.6.7";
    private Wallet wallet;
    private SignatureVerifier signatureVerifier;
    private Database database;
    private UserInformation myUserInformation;
    private MessageSender messageSender;
    private MessageListener receiver;
    private boolean exit;
    private Scanner scanner;
    private String command;
    private String username;
    private int unicastPort;
    private String coinPrice;
    private PeerWindow peerWindow;
    private InetAddress group;
    private MulticastSocket multicastSocket = null;
    private DebugThread debugThread;
    
    public Peer(String username, int unicastPort, String coinPrice){
        System.out.println("Peer Constructor");
//        this.keyHolder = new KeyHolder();
//        this.verifier = new SignatureVerifier();
//        this.wallets = new ArrayList();
//        this.myWallet = new Wallet(username, 100, this.keyHolder.getNotEncodedPublicKey());
        this.username = username;
        this.unicastPort = unicastPort;
        this.scanner = new Scanner(System.in);
        this.wallet = new Wallet();
        this.signatureVerifier = new SignatureVerifier();
        this.database = new Database();
        this.myUserInformation = new UserInformation(username, 100,coinPrice, unicastPort ,this.wallet.getPublicKey());
        this.database.addUserInformation(myUserInformation);
        this.coinPrice = coinPrice;
        peerWindow = new PeerWindow(myUserInformation, this);
        peerWindow.setVisible(true);
        updateDatabaseTable();
        System.out.println("End Peer Constructor");
    }
    
    public synchronized void databaseAddUserInformation(UserInformation userInformation){
        database.addUserInformation(userInformation);
        peerWindow.updateDatabase(database);
    }
    
    public synchronized void databaseRemoveUserInformation(UserInformation userInformation){
        database.removeUserInformation(userInformation);
        peerWindow.updateDatabase(database);
    }
    
    public void test_signature(){
        
        byte [] signedFile = wallet.signFile("test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file2.txt");
    }
    
    public void init_peer() {
        try {
            // Sets group settings and join multicast group
            group = InetAddress.getByName(GROUP_IP);
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
            multicastSocket.joinGroup(group);
            
            // Starts MessageListener Multicast thread
            receiver = new MessageListener(multicastSocket, this);
            receiver.start();
            
            receiver.startUDPServer(unicastPort);
            //System.out.println("Init Peer unicastPort: "+unicastPort);
            // Sends Hello Message at the start to the multicast group
            messageSender = new MessageSender(multicastSocket);
            System.out.println("I have just entered in this group! Sending Hello Message!");
            sendMulticastMessage("hello");
            exit = false;
        } catch (UnknownHostException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
        debugThread = new DebugThread(this);
        debugThread.start();
    }

    public void sendMulticastMessage(String command) {
        try{
            switch (command) {
                case "hello":
                    messageSender.sendHello(myUserInformation);
                    break;
                case "exit":
                    messageSender.sendExit(myUserInformation);
                    exit = true;
                    receiver.setExit(exit);
                    System.out.println("Exiting...");
                    break;
                case "help":
                    System.out.println("Commands Help:");
                    break;
                case "transaction":
                    //if (database.getNumberOfUsers() >= MIN_USERS) {
                        //sender = new MessageSender(multicastSocket);
                        messageSender.sendTransaction();
                    //} else {
                    //    System.out.println("ERROR | You may only perform a transaction when at least 4 users are in the network.");
                    //    System.out.println("      | There are currently " + database.getNumberOfUsers() + " users.");
                    //}
                    break;
                default:
                    System.out.println("ERROR | Command not found");
                    break;
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendUnicastMessage(String command, int unicastPort){
        try {
            switch (command) {
                case "database":
                messageSender.sendDatabase(database, unicastPort);
            }    
        } catch (IOException ex) {
            Logger.getLogger(Peer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String getUsername(){
        return username;
    }
    
    public void exitProgram() throws IOException{
        // Exit program
        multicastSocket.leaveGroup(group);
        if (multicastSocket != null)
            multicastSocket.close();
    }
    
    public synchronized void setDatabase(Database database){
        this.database = database;
        updateDatabaseTable();
    }
    
    public synchronized void updateDatabaseTable(){
        peerWindow.updateDatabase(database);
    }
    
    public void printDatabase(){
        database.printDatabase();
    }
    
}