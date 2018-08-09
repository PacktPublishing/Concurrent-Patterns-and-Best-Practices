package com.concurrency.book.chapter04.mypool;

import java.util.concurrent.BlockingQueue;

public class MyPoolThread extends Thread {

    private BlockingQueue taskQueue = null;
    private boolean isDone = false;

    public MyPoolThread(BlockingQueue queue) {
        taskQueue = queue;
    }

    public void run() {
        while (!isDone()) {
            try {
                Runnable runnable = (Runnable) taskQueue.take();
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void doStop() {
        isDone = true;
        this.interrupt(); //break pool thread out of dequeue() call.
    }

    public synchronized boolean isDone() {
        return isDone;
    }
}