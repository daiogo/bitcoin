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
    private String buyer;
    
    public BuyMessage(int coins, String buyer) {
        this.coins = coins;
        this.buyer = buyer;
    }
    
    public int getCoins() {
        return coins;
    }
    
    public String getBuyer() {
        return buyer;
    }
}
