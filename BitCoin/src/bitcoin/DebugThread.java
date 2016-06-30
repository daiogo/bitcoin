/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.util.Scanner;

/**
 *
 * @author diego
 */
public class DebugThread extends Thread{
    
    private Scanner scanner = new Scanner(System.in);
    private Peer myPeer;
    
    public DebugThread(Peer peer){
        myPeer = peer;
    }
    
    @Override
    public void run(){
        while(true){
            System.out.println("Debug command: ");  
            String command = scanner.nextLine();
            switch(command){
                case "database":
                    myPeer.printDatabase();
                    break;
                case "update":
                    System.out.println("Debug command: UpdateTable"); 
                    //myPeer.updateDatabaseTable();
                    break;
            }
        }
    }
}
