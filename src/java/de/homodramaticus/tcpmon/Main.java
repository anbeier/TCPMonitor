package de.homodramaticus.tcpmon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Main {

    public static void main(String[] args) {

        int port = 1234;
        String ipAddr = "0.0.0.0";
        int backlogSize = 10;
        int connectionTimeOutInMs = 100;
        int sleepDurationInMs = 1000;
        boolean listening = true;
        int bufferSize = 1024;

        if (args.length > 0) {
            listening = args[0].equals("1");
        }

        if (listening) {
            try {
                ServerSocket server = new ServerSocket(port, backlogSize, InetAddress.getByName(ipAddr));
                int connNum = 0;

                while (true) {
                    Socket client = server.accept();
                    connNum++;
                    System.out.printf("%d connection established%n", connNum);
                    // multi threading
                    try {
                        InputStream input = client.getInputStream();
                        OutputStream output = client.getOutputStream();

                        while (true) {
                            byte[] b = new byte[bufferSize];
                            int readBytes = input.read(b);

                            if (readBytes == -1) {
                                System.out.println("remote host closed the connection");
                                break;
                            }

                            output.write(b, 0, readBytes);
                        }
                    }
                    catch (IOException e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }

                    client.close();
                }

            }
            catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }

        } else {
            while (true) {
                try {
                    String msg = "tik tok";
                    byte[] msgBytes = msg.getBytes("UTF-8");

                    Socket socket = new Socket(InetAddress.getByName(ipAddr), port);
//                    socket.connect(socket.getRemoteSocketAddress(), connectionTimeOut);
                    socket.setSoTimeout(connectionTimeOutInMs);
                    System.out.println("connection to the server established");

                    while (true) {
                        try {
                            OutputStream output = socket.getOutputStream();
                            output.write(msgBytes);

                            byte[] response = new byte[msgBytes.length];
                            int position = 0;

                            while (position != response.length) {
                                InputStream input = socket.getInputStream();
                                int readBytes = input.read(response, position, response.length);
                                String responseText = new String(response, "UTF-8");
                                System.out.println(responseText);
                                position += readBytes;
                                System.out.printf("read %d posistion%n", position);
                            }

                            if (msgBytes != response) {
                                // exception
                            }
                        }
                        catch (IOException e) {
                            System.out.println(e.toString());
                            e.printStackTrace();
                        }

                        try {
                            Thread.sleep(sleepDurationInMs);
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                            socket.close();
                            break;
                        }
                    }
                }
                catch (IOException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(sleepDurationInMs);
                }
                catch (InterruptedException ie) {
                    ie.printStackTrace();
                    break;
                }
            }
        }
    }
}
