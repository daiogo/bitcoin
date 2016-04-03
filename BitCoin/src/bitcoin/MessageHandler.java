/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import bitcoin.messages.HelloMessage;
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
    private Peer myPeer;
    
    public MessageHandler(byte[] message, Peer peer) {
        this.message = message;
        myPeer = peer;
    }
    
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
    
    public void run() {
        Object object = deserialize_object(message);
        String objectName = object.getClass().getName();
        System.out.println("Class name: " + objectName);
        
        switch(objectName){
            case "bitcoin.messages.HelloMessage":
                handle_hello_message(HelloMessage.class.cast(object));
                break;
            default:
                System.out.println("Message received class not found: " + objectName);
                break;
        }
    }
    
    public void handle_hello_message(HelloMessage helloMessage){
        System.out.println("Received Hello Message");
        UserInformation userInformation = helloMessage.getUserInformation();
        //ignore my own message
        if(!userInformation.getUsername().equals(myPeer.getUsername())){
            myPeer.databaseAddUserInformation(helloMessage.getUserInformation());
        }
    }
}
