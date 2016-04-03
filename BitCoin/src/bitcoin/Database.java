/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Database {
    private ArrayList arrayUserInformation;
    private ArrayList arrayTransactions;
    private int numberOfUsers;
    private int numberOfTransactions;
    
    public Database() {
        this.arrayUserInformation = new ArrayList();
        this.arrayTransactions = new ArrayList();
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
