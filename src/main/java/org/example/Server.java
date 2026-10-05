package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {

    private static final List<ClientConnection> clients =
            Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {

        int port = 2000;

        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid port number.");
                return;
            }
        }

        final int serverPort = port;

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {

            String host = serverSocket.getInetAddress()
                    .getLocalHost()
                    .getHostName();

            printStatus(host, serverPort);

            while (true) {

                Socket clientSocket = serverSocket.accept();

                ClientConnection client =
                        new ClientConnection(clientSocket);

                clients.add(client);

                System.out.println(
                        "Client connected: " +
                                clientSocket.getInetAddress()
                );

                printStatus(host, serverPort);

                Runnable handleClient = () -> {

                    try {
                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(
                                                clientSocket.getInputStream(),
                                                "ISO-8859-1"
                                        )
                                );

                        String message;

                        while ((message = reader.readLine()) != null) {

                            System.out.println(
                                    "Client " +
                                            clientSocket.getInetAddress() +
                                            ": " +
                                            message
                            );

                            broadcast(message);
                        }

                    } catch (IOException e) {
                        System.out.println(
                                "Connection lost: " +
                                        clientSocket.getInetAddress()
                        );

                    } finally {

                        removeClient(client);

                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println(
                                    "Could not close client socket"
                            );
                        }

                        printStatus(host, serverPort);
                    }
                };

                Thread clientThread =
                        new Thread(handleClient);

                clientThread.start();
            }

        } catch (IOException e) {
            System.out.println("Server error.");
        }
    }

    private static synchronized void broadcast(String message) {
        for (ClientConnection client : clients) {
            client.getWriter().println(message);
        }
    }

    private static synchronized void removeClient(
            ClientConnection client
    ) {
        clients.remove(client);
    }

    private static void printStatus(
            String host,
            int port
    ) {
        System.out.println(
                "Server running on " +
                        host +
                        ":" +
                        port +
                        " | Clients connected: " +
                        clients.size()
        );
    }
}