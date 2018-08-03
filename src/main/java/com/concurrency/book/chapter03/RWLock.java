package com.concurrency.book.chapter03;

public interface RWLock {
    public void lock() throws InterruptedException;

    public void unlock();
}
