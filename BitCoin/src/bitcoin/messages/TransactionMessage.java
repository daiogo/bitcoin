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
public class TransactionMessage implements Serializable{
    private BuyMessage buyMessage;
    private byte[] encryptedBuyMessage;
    
    public TransactionMessage(BuyMessage buyMessage, 
            byte[] encryptedBuyMessage){
        this.buyMessage = buyMessage;
        this.encryptedBuyMessage = encryptedBuyMessage;
    }

    public BuyMessage getBuyMessage() {
        return buyMessage;
    }

    public byte[] getEncryptedBuyMessage() {
        return encryptedBuyMessage;
    }
    
    
}
