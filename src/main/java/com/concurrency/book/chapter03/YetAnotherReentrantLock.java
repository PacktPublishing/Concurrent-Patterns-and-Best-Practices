package com.concurrency.book.chapter03;

import java.util.LinkedList;
import java.util.Queue;

public class YetAnotherReentrantLock {
    private Thread lockedBy = null;
    private int lockCount = 0;

    private boolean isLocked() {
        return lockedBy != null;
    }

    private boolean isLockedByMe() {
        return Thread.currentThread() == lockedBy;
    }

    public synchronized void lock() throws InterruptedException {
        while (isLocked() && !isLockedByMe()) {
            this.wait();
        }
        lockedBy = Thread.currentThread();
        lockCount++;
    }

    public synchronized void unlock() {
        if (isLockedByMe()) {
            lockCount--;
        }
        if (lockCount == 0) {
            lockedBy = null;
            this.notify();
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
        final YetAnotherReentrantLock lock = new YetAnotherReentrantLock();

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    System.out.println("Putting elem " + i);
                    try {
                        lock.lock();
                        queue.add(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                    sleep(300);
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    System.out.println("Got elem " + i);
                    try {
                        lock.lock();
                        if (!queue.isEmpty()) {
                            System.out.println(queue.poll());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                    sleep(150);
                }
            }
        });

        producer.start();
        consumer.start();
    }

}
