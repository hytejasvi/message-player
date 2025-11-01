package com.example.player.messenger;

import com.example.player.model.Message;
import java.io.*;
import java.net.Socket;

/**
 * TCP-based Messenger implementation.
 */

public final class SocketMessenger implements Messenger {

    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public SocketMessenger(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true); // auto-flush
        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void send(Message message) throws IOException {
        String line = message.senderId() + "|" + message.payload();
        out.println(line);
        if (out.checkError()) {
            throw new IOException("Failed to send message");
        }
    }

    @Override
    public Message receive() throws IOException {
        String line = in.readLine();
        if (line == null) {
            throw new IOException("End reached");
        }
        int delim = line.indexOf('|');
        if (delim == -1) {
            throw new IOException("Malformed message: " + line);
        }
        String senderId = line.substring(0, delim);
        String payload  = line.substring(delim + 1);
        return new Message(senderId, payload);
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        socket.close();
    }
}

