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
    
    public boolean verify(PublicKey pubKey, byte[] signedMessage, byte[] originalMessage){
        /* Verify a DSA signature */
        if (signedMessage == null) {
            System.out.println("ERROR | SignatureVerifier | Signed message is null");
            return false;
        }
        try {
            
            /* create a Signature object and initialize it with the public key */
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pubKey);

            sig.update(originalMessage, 0, originalMessage.length);
            
            return sig.verify(signedMessage);
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        return false;   // Default return statement
    }
}
