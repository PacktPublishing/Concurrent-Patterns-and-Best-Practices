package com.concurrency.book.chapter04;

import java.util.Deque;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TaskStealingThread extends Thread {
    final Deque<Runnable>[] arrTaskQueue;
    private final int myId;
    final Random rand;

    public TaskStealingThread(Deque<Runnable>[] arrTaskQueue, int myId, Random rand) {
        this.arrTaskQueue = arrTaskQueue;
        this.myId = myId;
        this.rand = rand;
    }

    @Override
    public void run() {
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

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ConcurrentLinkedDeque[] taskQueue = new ConcurrentLinkedDeque[2];
        for (int i = 0; i < taskQueue.length; ++i) {
            taskQueue[i] = new ConcurrentLinkedDeque<Runnable>();
        }
        TaskStealingThread tst1 = new TaskStealingThread(taskQueue, 0, new Random());
        TaskStealingThread tst2 = new TaskStealingThread(taskQueue, 1, new Random());

        tst1.start();
        tst2.start();

        Random random = new Random();

        for (int i = 0; i < 10; ++i) {
            int duration = i;
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread " + Thread.currentThread().getName());
                    sleep(duration * 10);
                }
            };
            final int queueIndex = random.nextInt(taskQueue.length);
            taskQueue[queueIndex].add(r);
        }
    }
}
