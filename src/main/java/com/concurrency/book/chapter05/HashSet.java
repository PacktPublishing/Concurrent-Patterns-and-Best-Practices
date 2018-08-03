package com.concurrency.book.chapter05;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class HashSet<T> {
    final static int LIST_LEN_THRESHOLD = 100;
    protected List<T>[] table;
    protected AtomicInteger size;
    protected AtomicBoolean needsToResize;

    public HashSet(int capacity) {
        size = new AtomicInteger(0);
        needsToResize = new AtomicBoolean(false);
        for (int i = 0; i < capacity; ++i) {
            table[i] = new ArrayList<>();
        }
    }

    public boolean contains(T x) {
        lock(x);
        try {
            int bucket = x.hashCode() % table.length;
            return table[bucket].contains(x);
        } finally {
            unlock(x);
        }
    }

    public boolean add(T x) {
        boolean result = false;
        lock(x);
        try {
            int bucket = x.hashCode() % table.length;
            if (!table[bucket].contains(x)) {
                table[bucket].add(x);
                result = true;
                size.incrementAndGet();
                if (table[bucket].size() >= LIST_LEN_THRESHOLD)
                    needsToResize.set(true);
            }
        } finally {
            unlock(x);
        }
        if (shouldResize())
            resize();
        return result;
    }

    protected abstract void resize();

    protected abstract boolean shouldResize();

    protected abstract void unlock(T x);

    protected abstract void lock(T x);
}
