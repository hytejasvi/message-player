package com.example.player;

import com.example.player.messenger.InProcessMessenger;
import com.example.player.messenger.Messenger;
import com.example.player.player.InitiatorPlayer;
import com.example.player.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Same-JVM launcher.
 * Creates two threads, one initiator and one responder, then waits completion.
 */

public class Main {

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].equals("--mode=inprocess")) {
            System.err.println("Usage: java -jar message-player.jar --mode=inprocess");
            System.exit(1);
        }
        runInProcess();
    }

    private static void runInProcess() {
        AtomicBoolean shutdown = new AtomicBoolean(false);

        InProcessMessenger[] pair = InProcessMessenger.createPair("player1", "player2");
        Messenger m1 = pair[0];
        Messenger m2 = pair[1];

        InitiatorPlayer p1 = new InitiatorPlayer("player1", m1, shutdown);
        Player p2 = new Player("player2", m2, shutdown);

        p1.start();
        p2.start();

        try {
            p1.join();
            p2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("All done – exiting JVM");
    }
}


//mvn exec:java "-Dexec.mainClass=com.example.player.Main" "-Dexec.args=--mode=inprocess"