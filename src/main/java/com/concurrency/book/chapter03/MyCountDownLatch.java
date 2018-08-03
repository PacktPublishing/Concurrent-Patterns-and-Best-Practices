package com.concurrency.book.chapter03;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatch {
    private int cnt;
    private ReentrantLock lck;
    private Condition cond;

    public MyCountDownLatch(int cnt) {
        this.cnt = cnt;
        lck = new ReentrantLock();
        cond = lck.newCondition();
    }

    public void await() throws InterruptedException {
        lck.lock();
        try {
            while (cnt != 0) {
                cond.await();
            }

        } finally {
            lck.unlock();
        }
    }

    public void countDown() {
        lck.lock();
        try {
            --cnt;
            if (cnt == 0) {
                cond.signalAll();
            }
        } finally {
            lck.unlock();
        }
    }
}
