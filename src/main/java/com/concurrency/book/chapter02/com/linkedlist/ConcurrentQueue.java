package com.concurrency.book.chapter02.com.linkedlist;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentQueue {
    public static final int NElems = 20;
    final Lock lck = new ReentrantLock();
    final Condition needSpace = lck.newCondition();
    final Condition needElem = lck.newCondition();
    final int[] items;
    int tail, head, count;

    public ConcurrentQueue(int cap) {
        this.items = new int[cap];
    }

    public void push(int elem) throws InterruptedException {
        lck.lock();

        try {
            while (count == items.length)
                needSpace.await();
            items[tail] = elem;
            ++tail;
            if (tail == items.length)
                tail = 0;
            ++count;
            needElem.signal();
        } finally {
            lck.unlock();
        }
    }

    public int pop() throws InterruptedException {
        lck.lock();

        try {
            while (count == 0)
                needElem.await();
            int elem = items[head];
            ++head;
            if (head == items.length)
                head = 0;
            --count;
            needSpace.signal();
            return elem;
        } finally {
            lck.unlock();
        }
    }

    public static void main(String[] args) {
        final ConcurrentQueue cq = new ConcurrentQueue(10);
        final Runnable pusher = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < NElems; ++i) {
                    try {
                        cq.push(i);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        final Runnable popper = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < NElems; ++i) {
                    try {
                        final int elem = cq.pop();
                        System.out.println(elem);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };

        final Thread pusherThread = new Thread(pusher);
        final Thread popperThread = new Thread(popper);

        popperThread.start();
        pusherThread.start();

        System.out.println("Done");
    }
}
