package com.concurrency.book.chapter02;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

public abstract class ImprovedMyLock implements Lock {

    AtomicBoolean b = new AtomicBoolean(false);

    @Override
    public void lock() {
        while (true) {
            while (b.get()) { /* do nothing*/ }
            if (!b.getAndSet(true))
                return;
        }
    }

    @Override
    public void unlock() {
        b.set(false);
    }

    // other methods not shown
}
