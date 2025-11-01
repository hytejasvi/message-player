package com.example.player.player;

import com.example.player.messenger.Messenger;
import com.example.player.model.Message;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Generic responder player.
 * Receives messages, appends own counter, sends reply until shutdown.
 */

public class Player implements Runnable {

    protected final String id;
    protected final Messenger messenger;
    protected int counter = 0;
    protected final AtomicBoolean shutdown;
    private Thread worker;

    public Player(String id, Messenger messenger, AtomicBoolean shutdown) {
        this.id = id;
        this.messenger = messenger;
        this.shutdown = shutdown;
    }

    public void start() {
        worker = new Thread(this, id + "-Thread");
        worker.start();
    }

    public void stop() throws InterruptedException {
        worker.interrupt();
        worker.join(2_000);
    }

    @Override
    public void run() {
        try {
            while (!shutdown.get() && !Thread.currentThread().isInterrupted()) {
                Message in = messenger.receive();
                if ("__shutdown__".equals(in.payload())) {
                    System.out.println("[" + id + "]  shutdown signal received – exiting");
                    break;
                }
                String newPayload = in.payload() + ":" + (++counter);
                Message out = new Message(id, newPayload);
                messenger.send(out);
                System.out.printf("[%-7s]  RECV  : %s%n", id, in.payload());
                System.out.printf("[%-7s]  SENT  : %s%n", id, newPayload);
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

    public final void join() throws InterruptedException {
        worker.join();
    }
}