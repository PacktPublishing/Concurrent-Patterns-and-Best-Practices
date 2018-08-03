package com.concurrency.book.chapter02;

import java.util.concurrent.atomic.AtomicInteger;

public class NoLocksCounter {
    private final AtomicInteger c = new AtomicInteger(0);

    public int getValue() {
        return c.get();
    }

    public void inc() {
        while (true) {
            int currVal = getValue();
            int newVal = currVal + 1;
            if (c.compareAndSet(currVal, newVal)) {
                return;
            }
        }
    }
}
