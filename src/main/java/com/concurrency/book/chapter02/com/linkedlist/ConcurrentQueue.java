package com.concurrency.book.chapter02.com.linkedlist;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentQueue {
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

}
