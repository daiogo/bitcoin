/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin.messages;

import java.io.Serializable;

/**
 *
 * @author Diogo
 */
public class TransactionMessage implements Serializable {
    private String id;
    private byte[] serializedBuyMessage;
    private byte[] encryptedBuyMessage;
    
    public TransactionMessage(String id, byte[] serializedBuyMessage, byte[] encryptedBuyMessage) {
        this.id = id;
        this.serializedBuyMessage = serializedBuyMessage;
        this.encryptedBuyMessage = encryptedBuyMessage;
    }

    public String getId() {
        return id;
    }
    
    public byte[] getEncryptedBuyMessage() {
        return encryptedBuyMessage;
    }
    
    public byte[] getSerializedBuyMessage() {
        return serializedBuyMessage;
    }
}
