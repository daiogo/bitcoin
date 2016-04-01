/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitcoin;

import java.net.*;
import java.io.*;

public class TCPServer {
    public void start_server(int serverPort) {
        try{
            //int serverPort = 7896; // the server port
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while(true) {
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        } catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
    }

    private class Connection extends Thread {
	DataInputStream in;
	DataOutputStream out;
	Socket clientSocket;
        
	public Connection (Socket aClientSocket) {
            try {
                clientSocket = aClientSocket;
                in = new DataInputStream( clientSocket.getInputStream());
                out =new DataOutputStream( clientSocket.getOutputStream());
                this.start();
            } catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}
        
	public void run(){
            try {			                 // an echo server

                    String data = in.readUTF();	                  // read a line of data from the stream
                    out.writeUTF(data);
            }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
            } catch(IOException e) {System.out.println("readline:"+e.getMessage());
            } finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
		
	}
    }

}

