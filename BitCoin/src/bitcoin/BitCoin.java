/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.io.IOException;

/**
 *
 * @author diego
 */
public class BitCoin {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if(args.length < 3){
            System.out.println("Please use three arguments: ID PORT CoinPrice");
            System.exit(0);
        }
        /*
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(3000)+500);
        } catch (InterruptedException ex) {
            Logger.getLogger(BitCoin.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
	Peer peer = new Peer(args[0], Integer.parseInt(args[1]), args[2]);
	//peer.test_signature();
        peer.initPeer();
        //peer.start();
    }
    
}
