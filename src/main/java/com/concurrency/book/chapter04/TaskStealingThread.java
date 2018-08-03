package com.concurrency.book.chapter04;

import java.util.Deque;
import java.util.Random;

public class TaskStealingThread extends Thread {
    final Deque<Runnable>[] arrTaskQueue;
    final Random rand;

    public TaskStealingThread(Deque<Runnable>[] arrTaskQueue, Random rand) {
        this.arrTaskQueue = arrTaskQueue;
        this.rand = rand;
    }

    @Override
    public void run() {
        int myId = (int) getId();
        Deque<Runnable> myTaskQueue = arrTaskQueue[myId];
        Runnable task = null;
        if (!myTaskQueue.isEmpty()) {
            task = myTaskQueue.pop();
        }

        while(true) {
            while (task != null) {
                task.run();
                if (myTaskQueue.isEmpty()) {
                    task = null;
                } else {
                    task = myTaskQueue.removeFirst();
                }
            }

            while (task == null) {
                Thread.yield();
                int stealIndex = rand.nextInt(arrTaskQueue.length);
                if (!arrTaskQueue[stealIndex].isEmpty()) {
                    task = arrTaskQueue[stealIndex].removeLast();
                }
            }
        }
    }
}
