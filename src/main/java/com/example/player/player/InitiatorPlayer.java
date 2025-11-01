package com.example.player.player;

import com.example.player.messenger.Messenger;
import com.example.player.model.Message;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Player-1 (initiator) that starts the conversation to exactly 10 exchanges.
 */
public class InitiatorPlayer extends Player {

    private int sent = 0;
    private int received = 0;
    private static final int LIMIT = 10;

    public InitiatorPlayer(String id, Messenger messenger, AtomicBoolean shutdown) {
        super(id, messenger, shutdown);
    }

    @Override
    public void run() {
        try {
            String firstPayload = "hello";
            messenger.send(new Message(id, firstPayload));
            sent++;
            System.out.printf("[%-7s]  SENT  : %s  (%d/%d)%n", id, firstPayload, sent, LIMIT);

            while (!shutdown.get() && received < LIMIT) {
                Message in = messenger.receive();
                received++;
                System.out.printf("[%-7s]  RECV  : %s  (%d/%d)%n", id, in.payload(), received, LIMIT);

                if (received == LIMIT) {
                    shutdown.set(true);
                    messenger.send(new Message(id, "__shutdown__"));
                    break;
                }

                String newPayload = in.payload() + ":" + (++counter);
                messenger.send(new Message(id, newPayload));
                sent++;
                System.out.printf("[%-7s]  SENT  : %s  (%d/%d)%n", id, newPayload, sent, LIMIT);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            System.out.println("[" + id + "]  interrupted – exiting");
        } catch (IOException e) {
            System.out.println("[" + id + "]  I/O problem – exiting: " + e.getMessage());
        } finally {
            try {
                messenger.close();
            } catch (IOException ignore) {}
        }
    }
}
