/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.net.DatagramPacket;

/**
 *
 * @author Diogo
 */
public class MessageSender extends Thread {
    private DatagramPacket outPacket;
    private byte[] helloMessage;
    private byte[] transactionMessage;
        
    public MessageSender() {

    }
    
    public void sendHello() {
        
    }
    
    public void sendTransaction() {
        
    }
    
    @Override
    public void run() {
        
    }
}
