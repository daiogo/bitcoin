/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.security.PublicKey;

/**
 *
 * @author Diogo
 */
public class Wallet {
    private String username;
    private int coins;
    private PublicKey publicKey;
    
    public Wallet(String username, int coins, PublicKey publicKey) {
        this.username = username;
        this.coins = coins;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public int getCoins() {
        return coins;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
