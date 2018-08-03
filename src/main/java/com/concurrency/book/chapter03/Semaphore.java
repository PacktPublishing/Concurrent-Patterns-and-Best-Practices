package com.concurrency.book.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {
    private final int cap;
    private int count;
    private final Lock lck;
    private final Condition condition;

    public Semaphore(int cap) {
        this.cap = cap;
        count = 0;
        lck = new ReentrantLock();
        condition = lck.newCondition();
    }

    public void acquire() throws InterruptedException {
        lck.lock();
        try {
            while (count == cap) {
                condition.await();
            }
            count++;
        } finally {
            lck.unlock();
        }
    }

    public void release() {
        lck.lock();
        try {
            count--;
            condition.signalAll();
        } finally {
            lck.unlock();
        }
    }

}
