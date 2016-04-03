/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Database implements Serializable{
    private ArrayList arrayUserInformation;
    private ArrayList arrayTransactions;
    private int numberOfUsers;
    private int numberOfTransactions;
    
    public Database() {
        this.arrayUserInformation = new ArrayList();
        this.arrayTransactions = new ArrayList();
        numberOfUsers = 0;
        numberOfTransactions = 0;
    }
    
    public void addUserInformation(UserInformation userInformation){
        arrayUserInformation.add(userInformation);
    }

    public ArrayList getArrayUserInformation() {
        return arrayUserInformation;
    }

    public ArrayList getArrayTransactions() {
        return arrayTransactions;
    }

    public int getNumberOfUsers() {
        numberOfUsers = arrayUserInformation.size();
        return numberOfUsers;
    }

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

   
    
}
