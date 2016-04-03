/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Diogo
 */
public class MessageHandler extends Thread {
    private byte[] buffer;
    private Pattern usernamePattern;
    private Pattern pricePattern;
    private Pattern portPattern;
    private Pattern publicKeyPattern;
    private Matcher matcher;
    
    public MessageHandler(byte[] buffer) {
        this.buffer = buffer;
        this.usernamePattern = Pattern.compile(",,");
        this.pricePattern = Pattern.compile(",,");
        this.portPattern = Pattern.compile(",\\d{4},");
        this.publicKeyPattern = Pattern.compile(",[^,]+$");
    }
    
    public void run() {
        String message = new String(buffer).trim();
        System.out.println("Received: " + message);
        
        if (message.matches("^hello,.+$")) {
            System.out.println("This is a hello msg");
            // Add user to Peer's database
            if (matcher.find()) {
                
            }
        } else if (message.matches("^transaction,.*$")) {
            System.out.println("This is a transaction msg");
        } else {
            System.out.println("ERROR | Message corrupted");
        }
        
        
    }
}
