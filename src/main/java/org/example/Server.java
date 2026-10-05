package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private static List<Socket> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

        ServerSocket socket = null;
        int port = 2000;

        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
        }

        try {
            socket = new ServerSocket(port);
            System.out.println("Server started at " + port);
            while (true) {
                Socket clientSocket = socket.accept();
                System.out.println("Client connected");
                clients.add(clientSocket);
                System.out.println("Clients connected: " + clients.size());

                Runnable handleClient = () -> {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(
                                        clientSocket.getInputStream(),
                                        "ISO-8859-1"
                                )
                        );


                        while (true) {
                            String message = reader.readLine();
                            if(message == null){
                                break;
                            }
                            System.out.println("Client " + clientSocket.getInetAddress() + ": " + message);
                            synchronized (clients) {
                                for (Socket c : clients) {
                                    PrintWriter writer = new PrintWriter(
                                            new OutputStreamWriter(
                                                    c.getOutputStream(),
                                                    "ISO-8859-1"
                                            ), true
                                    );
                                    writer.println(message);
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Connection lost");
                    }
                    finally {
                        clients.remove(clientSocket);
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Could not close client socket");
                        }
                        System.out.println("Clients connected: " + clients.size());
                    }
                };
                Thread newClientThread = new Thread(handleClient);
                newClientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }

    }


}
