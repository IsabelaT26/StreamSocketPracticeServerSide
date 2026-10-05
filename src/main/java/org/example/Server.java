package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        ServerSocket socket = null;
        int port = 2000;

        if (args.length >= 2) {
            port = Integer.parseInt(args[1]);
        }

        try {
            socket = new ServerSocket(port);
            System.out.println("Server started at " + port);
            Socket clientSocket = socket.accept();
            if(clientSocket!=null) {
                System.out.println("Client connected");
            }
        }catch (IOException e){
            System.out.println("Error");
        }

    }


}
