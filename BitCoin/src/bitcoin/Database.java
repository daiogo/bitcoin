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
    
    public Database(){
        this.arrayUserInformation = new ArrayList();
        this.arrayTransactions = new ArrayList();
    }
}
