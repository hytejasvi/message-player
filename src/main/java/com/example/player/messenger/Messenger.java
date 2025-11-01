package com.example.player.messenger;

import com.example.player.model.Message;
import java.io.IOException;

/**
 * Communication strategy.
 * Implementations provide either in-process queue or TCP socket messaging.
 */
public interface Messenger {

    void send(Message message) throws IOException;

    Message receive() throws IOException, InterruptedException;

    void close() throws IOException;
}
