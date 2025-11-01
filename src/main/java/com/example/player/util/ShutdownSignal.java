package com.example.player.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simple flag to handle graceful shutdown across threads/JVMs.
 */

public final class ShutdownSignal {
    private static final AtomicBoolean DONE = new AtomicBoolean(false);

    public static boolean isDone() { return DONE.get(); }
    public static void stop()      { DONE.set(true); }
}