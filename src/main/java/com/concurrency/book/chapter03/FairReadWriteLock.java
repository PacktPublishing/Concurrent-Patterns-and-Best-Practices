package com.concurrency.book.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairReadWriteLock {
    int readersIn, readersOut;
    boolean writer;
    Lock lck;
    Condition condition;
    RWLock rdLock, wrLock;

    public FairReadWriteLock() {
        readersIn = readersOut = 0;
        writer = false;
        lck = new ReentrantLock();
        condition = lck.newCondition();
        rdLock = new RdLock();
        wrLock = new WrLock();
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
                readersIn++;
            } finally {
                lck.unlock();
            }

        }

        public void unlock() {
            lck.lock();
            try {
                readersOut++;
                if (readersIn == readersOut) {
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
                while (writer) {
                    condition.await();
                }
                writer = true;
                while (readersIn != readersOut) {
                    condition.await();
                }
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
