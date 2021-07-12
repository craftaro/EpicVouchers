package com.songoda.epicvouchers.utils;

import java.util.concurrent.atomic.AtomicReference;

// TODO: Copied from EpicAnchors - Move to SongodaCore
public class ThreadSync {
    private final Object syncObj = new Object();
    private final AtomicReference<Boolean> waiting = new AtomicReference<>(true);

    public void waitForRelease() {
        synchronized (syncObj) {
            while (waiting.get()) {
                try {
                    syncObj.wait();
                } catch (Exception ignore) {
                }
            }
        }
    }

    public void release() {
        synchronized (syncObj) {
            waiting.set(false);
            syncObj.notifyAll();
        }
    }

    public void reset() {
        waiting.set(true);
    }
}
