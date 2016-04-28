/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.security.*;

/**
 *
 * @author diego
 */
public class Wallet {
    
    private PrivateKey privateKey; 
    private PublicKey publicKey;
    
    public Wallet(){
        generateKeys();
    }
    
    public byte[] getEncodedPublicKey() {
        return publicKey.getEncoded();
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    private void generateKeys() {
        
        /* Generate a DSA signature */
        try{
            /* Generate a key pair */
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");

            keyGen.initialize(1024, random);

            KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
            
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
    
    public byte[] signFile(byte[] buffer) {
        byte[] realSig = null;

        try {
            /* Create a Signature object and initialize it with the private key */

            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 

            dsa.initSign(privateKey);
            
            dsa.update(buffer, 0, buffer.length);

            realSig = dsa.sign();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return realSig;
    }
}
