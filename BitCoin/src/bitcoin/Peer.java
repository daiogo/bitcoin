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
    public static final int PORT = 6789;
    public static final String GROUP_IP = "228.5.6.7";
    
    private Wallet wallet;
    private SignatureVerifier signatureVerifier;
    private Database database;
    private UserInformation myUserInformation;
    private MessageSender sender;
    private MessageReceiver receiver;
    private boolean exit;
    private Scanner scanner;
    private String command;
    
    public Peer(String username){
        System.out.println("Peer Constructor");
//        this.keyHolder = new KeyHolder();
//        this.verifier = new SignatureVerifier();
//        this.wallets = new ArrayList();
//        this.myWallet = new Wallet(username, 100, this.keyHolder.getNotEncodedPublicKey());
        this.scanner = new Scanner(System.in);
        this.wallet = new Wallet();
        this.signatureVerifier = new SignatureVerifier();
        this.database = new Database();
        this.myUserInformation = new UserInformation(username, 100, this.wallet.getPublicKey());
    }
    
    public void test_signature(){
        
        byte [] signedFile = wallet.signFile("test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file.txt");
        signatureVerifier.verify(myUserInformation.getPublicKey(), signedFile, "test_file2.txt");
    }
    
    public void start() {
        MulticastSocket s = null;
        
        try {
            // Sets group settings and join multicast group
            InetAddress group = InetAddress.getByName(GROUP_IP);
            s = new MulticastSocket(PORT);
            s.joinGroup(group);

            receiver = new MessageReceiver(s);
            receiver.start();

            while (exit == false) {
                System.out.println("Please enter you command:");
                command = scanner.nextLine();
                
                switch (command) {
                    case "exit":
                        exit = true;
                        receiver.setExit(exit);
                        System.out.println("Exiting...");
                        break;
                    case "hello":
                        // Sends hello message to group
                        //String helloMsg = "hello|" + myWallet.getPublicKey().toString() + "|" + myWallet.getCoins();
                        String helloMsg = "hello|" + "public key" + "|" + "coins";
                        this.sender = new MessageSender(s, helloMsg);
                        sender.start();
                        break;
                    default:
                        System.out.println("ERROR | Command not found");
                        break;
                }
            }
            
            // Exit program
            s.leaveGroup(group);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            if (s != null)
                s.close();
        }
    }    
}