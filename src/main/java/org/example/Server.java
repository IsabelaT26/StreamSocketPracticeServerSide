package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {

        ServerSocket socket = null;

        try {
            socket = new ServerSocket(2000);
        }catch (IOException e){
            System.out.println("Error");
        }

    }


}
