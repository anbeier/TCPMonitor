package de.homodramaticus.tcpmon;

import java.io.IOException;
import java.net.*;

public class Main {

    public static void main(String[] args) {

        int port = 1234;
        String ipAddr = "0.0.0.0";
        int backlogSize = 10;
        boolean listening = true;

        // create server socket
        // establish a conn
        // read
        try {
            ServerSocket serverSocket = new ServerSocket(port, backlogSize, InetAddress.getByName(ipAddr));

            while (listening) {
                Socket client = serverSocket.accept();
                System.out.println("connection established");
                // create a thread to deal with the client
            }
        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

    }
}
