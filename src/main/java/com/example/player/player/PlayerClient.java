package com.example.player.player;

import com.example.player.messenger.Messenger;
import com.example.player.messenger.SocketMessenger;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Stand-alone initiator program for multi-process mode.
 * Connects to PlayerServer, runs InitiatorPlayer logic, exits after 10 exchanges.
 */

public class PlayerClient {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java PlayerClient <host> <port>");
            System.exit(1);
        }
        String host = args[0];
        int port    = Integer.parseInt(args[1]);

        try (var socket = new Socket(host, port)) {
            System.out.println("player1 connected to " + host + ":" + port);
            AtomicBoolean shutdown = new AtomicBoolean(false);

            Messenger messenger = new SocketMessenger(socket);
            InitiatorPlayer initiator = new InitiatorPlayer("player1", messenger, shutdown);
            initiator.run();
            System.out.println("player1 finished");
        }
    }
}