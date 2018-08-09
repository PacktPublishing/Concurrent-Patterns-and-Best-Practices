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

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(3);

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Integer i = 0; i < 5; ++i) {
                    try {
                        semaphore.acquire();
                        System.out.println("Acquired " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Integer i = 0; i < 5; ++i) {
                    semaphore.release();
                    System.out.println("Released " + i);
                }
            }
        });

        producer.start();
        sleep(300);
        consumer.start();
    }
}
