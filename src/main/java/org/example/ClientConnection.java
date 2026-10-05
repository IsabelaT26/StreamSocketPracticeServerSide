package org.example;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ClientConnection {
    private final Socket socket;
    private final PrintWriter writer;

    ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = new PrintWriter(
                new OutputStreamWriter(
                        socket.getOutputStream(),
                        "ISO-8859-1"
                ),
                true
        );
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}