package com.concurrency.book.chapter02;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public abstract class MyLock implements Lock {

    AtomicBoolean b = new AtomicBoolean(false);

    @Override
    public void lock() {
        while(b.getAndSet(true)) {
            // do nothing
        }
    }

    @Override
    public void unlock() {
        b.set(false);
    }

    // other methods not shown
}
