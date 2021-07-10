package com.songoda.epicvouchers.utils;

import java.util.Map;
import java.util.WeakHashMap;

public class CachedSet<K> {
    private final Map<K, Long> cache = new WeakHashMap<>();
    private final int ttl;

    private long lastClear = System.currentTimeMillis();

    /**
     * @param ttl Time-To-Live in seconds
     */
    public CachedSet(int ttl) {
        this.ttl = ttl * 1000;
    }

    public void add(K obj) {
        this.cache.put(obj, System.currentTimeMillis());
    }

    public boolean contains(K obj) {
        if (shouldClear()) {
            clearStale();
        }

        return this.cache.computeIfPresent(obj, (k, aLong) -> System.currentTimeMillis()) != null;
    }

    public void clearStale() {
        this.cache.entrySet().removeIf(entry -> System.currentTimeMillis() - entry.getValue() >= ttl);

        this.lastClear = System.currentTimeMillis();
    }

    private boolean shouldClear() {
        return !this.cache.isEmpty() && System.currentTimeMillis() - lastClear > ttl;
    }
}
