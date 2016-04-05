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
public class BuyMessage implements Serializable {
    private int coins;
    private String buyerUsername;
    private String sellerUsername;
    
    public BuyMessage(int coins, String buyerUsername, String sellerUsername) {
        this.coins = coins;
        this.buyerUsername = buyerUsername;
        this.sellerUsername = sellerUsername;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public String getBuyerUsername() {
        return buyerUsername;
    }
    
    public String getSellerUsername() {
        return sellerUsername;
    }
}
