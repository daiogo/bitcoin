import java.net.*;
import java.io.*;
import java.security.*;

public class BitCoinPeer {
  public static void main(String args[]) {
    // args give message contents and destination multicast group (e.g.
    // "228.5.6.7")
    MulticastSocket s = null;
    try {
      InetAddress group = InetAddress.getByName("228.5.6.7");
      s = new MulticastSocket(6789);
      s.joinGroup(group);
      String string = "Hello";
      byte[] m = string.getBytes();
      DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
      s.send(messageOut);


    // Generate a 1024-bit Digital Signature Algorithm (DSA) key pair
      try {
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    keyGen.initialize(1024);
    KeyPair keypair = keyGen.genKeyPair();
    PrivateKey privateKey = keypair.getPrivate();
    System.out.println(privateKey.getEncoded());
    PublicKey publicKey = keypair.getPublic();
    System.out.println("PUBLICAAAAAAAAA");
    System.out.println(publicKey.getEncoded());
    } catch (NoSuchAlgorithmException e){

    }

      byte[] buffer = new byte[1000];
      for (int i = 0; i < 4; i++) { // get messages from others in group
        DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
        s.receive(messageIn);
        System.out.println("Received:" + new String(messageIn.getData()));
      }
      s.leaveGroup(group);
    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    } finally {
      if (s != null)
        s.close();
    }
  }
}