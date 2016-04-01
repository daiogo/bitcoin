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
public class MessageReceiver extends Thread {
    private DatagramPacket inPacket;
    private MulticastSocket socket;
    private boolean exit;
    private byte[] buffer;
        
    public MessageReceiver(MulticastSocket socket) throws UnknownHostException {
        this.socket = socket;
        this.exit = false;
        this.buffer = new byte[1000];
    }
    
    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    public void run() {
        while (exit == false) {
            try {
                inPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(inPacket);
                System.out.println("Received:" + new String(inPacket.getData()));
            } catch (IOException ex) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Receiver thread finished");
        //socket.close();
    }
    
}
