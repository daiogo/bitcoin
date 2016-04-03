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
    private MessageSender sender;
    private MessageListener receiver;
    private boolean exit;
    private Scanner scanner;
    private String command;
    private String username;
    private int unicastPort;
    private String coinPrice;
    private PeerWindow peerWindow;
    
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
        this.database.addUserInformation(this.myUserInformation);
        this.coinPrice = coinPrice;
        peerWindow = new PeerWindow(myUserInformation);
        peerWindow.setVisible(true);
        peerWindow.updateDatabase(database);
    }
    
    public void test_signature(){
        
        byte [] signedFile = wallet.signFile("test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file2.txt");
    }
    
    public void init_peer() {
        MulticastSocket multicastSocket = null;
        
        try {
            // Sets group settings and join multicast group
            InetAddress group = InetAddress.getByName(GROUP_IP);
            multicastSocket = new MulticastSocket(MULTICAST_PORT);
            multicastSocket.joinGroup(group);
            
            // Starts MessageListener thread
            receiver = new MessageListener(multicastSocket);
            receiver.start();
            
            // Sends Hello Message at the start to the multicast group
            sender = new MessageSender(multicastSocket);
            System.out.println("I have just entered in this group! Sending Hello Message!");
            sender.sendHello(username,coinPrice,unicastPort,wallet.getPublicKey());

            exit = false;
            while (exit == false) {
                System.out.println("Please enter your command:");
                command = scanner.nextLine();
                
                switch (command) {
                    case "exit":
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
                            sender.sendTransaction();
                        //} else {
                        //    System.out.println("ERROR | You may only perform a transaction when at least 4 users are in the network.");
                        //    System.out.println("      | There are currently " + database.getNumberOfUsers() + " users.");
                        //}
                        break;
                    default:
                        System.out.println("ERROR | Command not found");
                        break;
                }
            }
            
            // Exit program
            multicastSocket.leaveGroup(group);
        } catch (Exception e) {
            System.out.println("Peer Start Exception: " + e.getMessage());
        } finally {
            if (multicastSocket != null)
                multicastSocket.close();
        }
    }    
}