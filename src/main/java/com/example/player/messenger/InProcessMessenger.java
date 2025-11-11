package com.example.player.messenger;

import com.example.player.model.Message;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Same-JVM messenger backed by two blocking queues.
 * Creates a full-duplex channel between two threads.
 */

public final class InProcessMessenger implements Messenger {

    private final BlockingQueue<String> outboundQueue;
    private final BlockingQueue<String> inboundQueue;
    private final String ownerId;

    public static InProcessMessenger[] createPair(String id1, String id2) {
        BlockingQueue<String> q12 = new LinkedBlockingQueue<>();
        BlockingQueue<String> q21 = new LinkedBlockingQueue<>();
        return new InProcessMessenger[]{
                new InProcessMessenger(id1, q12, q21),
                new InProcessMessenger(id2, q21, q12)
        };
    }

    private InProcessMessenger(String ownerId,
                               BlockingQueue<String> outboundQueue,
                               BlockingQueue<String> inboundQueue) {
        this.ownerId = ownerId;
        this.outboundQueue = outboundQueue;
        this.inboundQueue = inboundQueue;
    }

    @Override
    public void send(Message message) {
        String line = message.senderId() + "|" + message.payload();
        try {
            outboundQueue.put(line);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Message receive() throws InterruptedException {
        String line = inboundQueue.poll(5, TimeUnit.SECONDS);
        if (line == null) {
            throw new InterruptedException("Timeout waiting for message");
        }
        int delimiter = line.indexOf('|');
        if (delimiter == -1) throw new IllegalArgumentException("Bad format: " + line);
        String senderId = line.substring(0, delimiter);
        String payload = line.substring(delimiter + 1);
        return new Message(senderId, payload);
    }

    @Override
    public void close() throws IOException {
    }
}
