/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import bitcoin.messages.MiningMessage;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class Database implements Serializable {
    private ArrayList <UserInformation> arrayUserInformation;
    private ArrayList <MiningMessage> arrayTransactions;
    private int numberOfUsers;
    private int numberOfTransactions;
    
    public Database() {
        this.arrayUserInformation = new ArrayList<>();
        this.arrayTransactions = new ArrayList<>();
        numberOfUsers = 0;
        numberOfTransactions = 0;
    }
    
    public synchronized void addUserInformation(UserInformation userInformation) {
        arrayUserInformation.add(userInformation);
    }
    
    public synchronized void removeUserInformation(UserInformation userInformation) {
        System.out.println("Remove User: " + userInformation.getUsername());
        //The hashCode() and equals() are a bit different because of serialization?
        //so remove(object) does not work
        //System.out.println("Contains? " + arrayUserInformation.contains(userInformation));
        
        for (int i = 0; i < arrayUserInformation.size(); i++) {
            UserInformation temp = (UserInformation) arrayUserInformation.get(i);
            if (temp.getUsername().equals(userInformation.getUsername())){
                arrayUserInformation.remove(i);
                break;
            }
        }
        
    }
    
    public PublicKey getPublicKey(String username) {
        for (int i=0; i < arrayUserInformation.size(); i++) {
            UserInformation temp = (UserInformation) arrayUserInformation.get(i);
            if (temp.getUsername().equals(username)) {
                return temp.getPublicKey();
            }
        }
        return null;
    }
    
    public int getUnicastPort(String username) {
        for (int i=0; i < arrayUserInformation.size(); i++) {
            UserInformation temp = (UserInformation) arrayUserInformation.get(i);
            if (temp.getUsername().equals(username)) {
                return temp.getUnicastPort();
            }
        }
        return 0;
    }

    public ArrayList<UserInformation> getArrayUserInformation() {
        return arrayUserInformation;
    }

    public ArrayList<MiningMessage> getArrayTransactions() {
        return arrayTransactions;
    }

    public int getNumberOfUsers() {
        numberOfUsers = arrayUserInformation.size();
        return numberOfUsers;
    }

    public int getNumberOfTransactions() {
        numberOfTransactions = arrayTransactions.size();
        return numberOfTransactions;
    }

    public void printDatabase() {
        System.out.println("Print Database: ");
        for (int i=0; i < arrayUserInformation.size(); i++){
            System.out.println(arrayUserInformation.get(i).getUsername());
        }    
    }

}
