package com.concurrency.book.chapter04.mypool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MyThreadPool {
    private BlockingQueue taskQueue = null;
    private List<MyPoolThread> threads = new ArrayList<MyPoolThread>();
    private boolean isDone = false;

    public MyThreadPool(int numThreads, int maxNumOfTasks) {
        taskQueue = new ArrayBlockingQueue(maxNumOfTasks);

        for (int i = 0; i < numThreads; i++) {
            threads.add(new MyPoolThread(taskQueue));
        }
        for (MyPoolThread thread : threads) {
            thread.start();
        }
    }

    public synchronized void execute(Runnable task) throws InterruptedException {
        if (this.isDone) throw
                new RuntimeException("ThreadPool is stopped");

        this.taskQueue.put(task);
    }

    public synchronized void stop() {
        this.isDone = true;
        for (MyPoolThread thread : threads) {
            thread.doStop();
        }
    }

}
