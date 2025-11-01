package com.example.player.player;

import com.example.player.messenger.Messenger;
import com.example.player.messenger.SocketMessenger;

import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Stand-alone responder program for multi-process mode.
 * Listens on TCP port, runs Player logic, exits when socket closes.
 */
public class PlayerServer {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java PlayerServer <port>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("player2 listening on port " + port);
            var socket = serverSocket.accept();
            System.out.println("player2 connected");

            AtomicBoolean shutdown = new AtomicBoolean(false);
            Messenger messenger = new SocketMessenger(socket);
            Player responder = new Player("player2", messenger, shutdown);
            responder.start();
            responder.join();
            System.out.println("player2 finished");
        }
    }
}