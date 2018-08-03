package com.concurrency.book.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteLock {
    int readers;
    boolean writer;
    Lock lck;
    Condition condition;
    RWLock rdLock, wrLock;

    public ReadWriteLock() {
        writer = false;
        readers = 0;
        lck = new ReentrantLock();
        rdLock = new RdLock();
        wrLock = new WrLock();
        condition = lck.newCondition();
    }

    public RWLock getRdLock() {
        return rdLock;
    }

    public RWLock getWrLock() {
        return wrLock;
    }

    class RdLock implements RWLock {

        public void lock() throws InterruptedException {
            lck.lock();
            try {
                while (writer) {
                    condition.await();
                }
                readers++;
            } finally {
                lck.unlock();
            }

        }

        public void unlock() {
            lck.lock();
            try {
                readers--;
                if (readers == 0) {
                    condition.signalAll();
                }
            } finally {
                lck.unlock();
            }

        }

    }

    class WrLock implements RWLock {

        public void lock() throws InterruptedException {
            lck.lock();
            try {
                while (readers > 0 || writer) {
                    condition.await();
                }
                writer = true;
            } finally {
                lck.unlock();
            }

        }

        public void unlock() {
            lck.lock();
            try {
                writer = false;
                condition.signalAll();
            } finally {
                lck.unlock();
            }

        }

    }

}
