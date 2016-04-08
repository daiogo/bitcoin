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
public class MiningMessage implements Serializable {
    private String id;
    private int ammount;
    private String sellerUsername;
    private String buyerUsername;
    private String minerUsername;
    private long timestamp;
    
    public MiningMessage(String id, int ammount, String sellerUsername, String buyerUsername, String minerUsername, long timestamp) {
        this.id = id;
        this.ammount = ammount;
        this.sellerUsername = sellerUsername;
        this.buyerUsername = buyerUsername;
        this.minerUsername = minerUsername;
        this.timestamp = timestamp;
    }
    
    public String getId() {
        return id;
    }

    public int getAmmount() {
        return ammount;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getBuyerUsername() {
        return buyerUsername;
    }

    public String getMinerUsername() {
        return minerUsername;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
}
