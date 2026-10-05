package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

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

                Runnable handleClient = () -> {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream())
                        );

                        while (true) {
                            String message = reader.readLine();
                            if(message == null){
                                break;
                            }
                            System.out.println("Client " + clientSocket.getInetAddress() + ": " + message);
                        }
                    } catch (IOException e) {
                        System.out.println("Connection lost");
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
