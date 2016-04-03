/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin.messages;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author diego
 */

public class HelloMessage implements Serializable{

    public String username;
    public int coinPrice;
    public int unicast_port;
    public PublicKey publicKey;

    public HelloMessage(String username, int coinPrice, int unicast_port, PublicKey publicKey){
         this.username = username;
         this.coinPrice = coinPrice;
         this.unicast_port = unicast_port;
         this.publicKey = publicKey;
    }  
}