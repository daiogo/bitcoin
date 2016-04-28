/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin.messages;

import bitcoin.UserInformation;
import java.io.Serializable;

/**
 *
 * @author diego
 */
public class ExitMessage implements Serializable{
    private UserInformation userInformation;
    
    public ExitMessage(UserInformation userInformation){
        this.userInformation = userInformation;
    }
    
    public UserInformation getUserInformation(){
        return userInformation;
    }
}
