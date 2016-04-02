/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.util.Arrays;

/**
 *
 * @author Diogo
 */
public class MessageHandler extends Thread {
    private byte[] message;
    
    public MessageHandler(byte[] message) {
        this.message = message;
    }
    
    public void run() {
        System.out.println("Received: " + new String(message));
    }
}
