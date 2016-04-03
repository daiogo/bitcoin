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
        if(args.length < 3){
            System.out.println("Please use three arguments: ID PORT CoinPrice");
            System.exit(0);
        }
	Peer peer = new Peer(args[0], Integer.parseInt(args[1]), args[2]);
	//peer.test_signature();
        peer.init_peer();
        //peer.start();
    }
    
}
