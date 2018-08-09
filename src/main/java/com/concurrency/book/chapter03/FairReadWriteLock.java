package com.concurrency.book.chapter03;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairReadWriteLock {
    private int readersIn, readersOut;
    private boolean writer;
    private Lock lck;
    private Condition condition;
    private RWLock rdLock, wrLock;

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

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final Queue<Integer> queue = new LinkedList<>();
        final FairReadWriteLock lock = new FairReadWriteLock();

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                final RWLock wrLock = lock.getWrLock();
                for (int i = 0; i < 50; ++i) {
                    System.out.println("Putting elem " + i);
                    try {
                        wrLock.lock();
                        queue.add(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        wrLock.unlock();
                    }
                    sleep(300);
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                final RWLock rdLock = lock.getRdLock();
                for (int i = 0; i < 50; ++i) {
                    System.out.println("Got elem " + i);
                    try {
                        rdLock.lock();
                        if (!queue.isEmpty()) {
                            System.out.println(queue.poll());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        rdLock.unlock();
                    }
                    sleep(150);
                }
            }
        });

        producer.start();
        consumer.start();
    }

}
