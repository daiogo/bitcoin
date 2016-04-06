package bitcoin;

import bitcoin.messages.BuyMessage;
import java.io.*;
import java.security.*;
import java.security.spec.*;
/**
 *
 * @author diego
 */
public class SignatureVerifier {
    
    public void verify(PublicKey pubKey, byte[] signedMessage, byte[] originalMessage){
        /* Verify a DSA signature */

        if(signedMessage == null){
            System.out.println("ERROR | SignatureVerifier | Signed message is null");
            return;
        }
        try{

            /* input the signature bytes */
            /*
            FileInputStream sigfis = new FileInputStream(signaturefile);
            byte[] sigToVerify = new byte[sigfis.available()]; 
            sigfis.read(sigToVerify );

            sigfis.close();
            */
            
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);

            /* Update and verify the data */
/*
            FileInputStream datafis = new FileInputStream(datafile);
            BufferedInputStream bufin = new BufferedInputStream(datafis);

            byte[] buffer = new byte[1024];
            int len;
            while (bufin.available() != 0) {
                len = bufin.read(buffer);
                sig.update(buffer, 0, len);
                };

            bufin.close();
  */          
            
            sig.update(originalMessage, 0, originalMessage.length);
            


            boolean verifies = sig.verify(signedMessage);

            System.out.println("signature verifies: " + verifies);


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
}
