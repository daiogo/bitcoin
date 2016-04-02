/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class MessageListener extends Thread {
    public static final int MAX_UDP_MESSAGE_SIZE = 65535;
    private MessageHandler handler;
    private DatagramPacket inPacket;
    private MulticastSocket socket;
    private boolean exit;
        
    public MessageListener(MulticastSocket socket) throws UnknownHostException {
        this.socket = socket;
        this.exit = false;
    }
    
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void run() {
        while (exit == false) {
            try {
                byte buffer[] = new byte[MAX_UDP_MESSAGE_SIZE];
                inPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(inPacket);
                handler = new MessageHandler(inPacket.getData());
                handler.start();
            } catch (IOException ex) {
                Logger.getLogger(MessageListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Receiver thread finished");
    }
    
}
