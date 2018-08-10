package com.concurrency.book.chapter05;

import com.concurrency.book.Utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeQueue<T> {
    protected class Node {
        public T value;
        public volatile Node next;

        public Node(T value) {
            this.value = value;
            this.next = null;
        }
    }

    private ReentrantLock enqLock, deqLock;
    Condition notEmptyCond, notFullCond;
    AtomicInteger size;
    volatile Node head, tail;
    final int capacity;

    public ThreadSafeQueue(int cap) {
        capacity = cap;
        head = new Node(null);
        tail = head;
        size = new AtomicInteger(0);
        enqLock = new ReentrantLock();
        deqLock = new ReentrantLock();
        notFullCond = enqLock.newCondition();
        notEmptyCond = deqLock.newCondition();
    }

    public void enq(T x) throws InterruptedException {
        boolean awakeConsumers = false;
        enqLock.lock();
        try {
            while (size.get() == capacity)
                notFullCond.await();
            Node e = new Node(x);
            tail.next = e;
            tail = e;
            if (size.getAndIncrement() == 0)
                awakeConsumers = true;
        } finally {
            enqLock.unlock();
        }
        if (awakeConsumers) {
            deqLock.lock();
            try {
                notEmptyCond.signalAll();
            } finally {
                deqLock.lock();
            }
        }
    }

    public T deq() throws InterruptedException {
        T result;
        boolean awakeProducers = false;
        deqLock.lock();
        try {
            while (size.get() == 0)
                notEmptyCond.await();
            result = head.next.value;
            head = head.next;
            if (size.getAndDecrement() == capacity)
                awakeProducers = true;
        } finally {
            deqLock.unlock();
        }
        if (awakeProducers) {
            enqLock.lock();
            try {
                notFullCond.signalAll();
            } finally {
                enqLock.unlock();
            }
        }
        return result;
    }

    public static void main(String[] args) {
        final ThreadSafeQueue<Integer> cq = new ThreadSafeQueue(4);
        int nElems = 50;

        final Thread enqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    try {
                        cq.enq(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ThreadUtils.sleep(10);
                }
            }
        });
        final Thread deqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    final int elem;
                    try {
                        elem = cq.deq();
                        System.out.println(elem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ThreadUtils.sleep(20);
                }
            }
        });

        enqThread.start();
        ThreadUtils.sleep(100);
        deqThread.start();

        System.out.println("Done");
    }
}
