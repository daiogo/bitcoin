/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

/**
 *
 * @author diego
 */
public class BitCoin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Please use two arguments: ID PORT");
            System.exit(0);
        }
	Peer peer = new Peer(args[0], args[1]);
	peer.test_signature();
        peer.start();
        //peer.start();
    }
    
}
