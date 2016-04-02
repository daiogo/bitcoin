/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo
 */
public class MessageHandler extends Thread {
    private byte[] message;
       
    public static Object deserialize_object(byte[] message) {
        ObjectInputStream objIn = null;
        Object object = null;
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(message);
            objIn = new ObjectInputStream(byteIn);
            object = objIn.readObject();
            return object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objIn.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return object;
    }
    
    public MessageHandler(byte[] message) {
        this.message = message;
    }
    
    public void run() {
        System.out.println("Received: " + new String(message));
    }
}
