package com.concurrency.book.chapter05;

import com.concurrency.book.Utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicReference;

public class NoLocksQueue<T> {
    protected class Node {
        public T value;
        public AtomicReference<Node> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    volatile AtomicReference<Node> head, tail;

    public NoLocksQueue() {
        final Node sentinel = new Node(null);
        head = new AtomicReference<>(sentinel);
        tail = new AtomicReference<>(sentinel);
    }

    public void enque(T v) {
        Node myTailNode = new Node(v);
        while (true) {
            Node currTailNode = tail.get();
            Node next = currTailNode.next.get();
            if (next == null) {
                if (currTailNode.next.compareAndSet(next, myTailNode)) {
                    tail.compareAndSet(currTailNode, myTailNode);
                    return;
                }
            } else {
                tail.compareAndSet(currTailNode, next);
            }
        }
    }

    public T deque() {
        while (true) {
            Node myHead = head.get();
            Node myTail = tail.get();
            Node next = myHead.next.get();

            if (myHead == head.get()) {
                if (myHead == myTail) {
                    if (next == null) {
                        throw new QueueIsEmptyException();
                    }
                    tail.compareAndSet(myTail, next);
                } else {
                    T value = next.value;
                    if (head.compareAndSet(myHead, next))
                        return value;
                }
            }
        }
    }

    public static void main(String[] args) {
        final NoLocksQueue<Integer> cq = new NoLocksQueue();
        int nElems = 50;

        final Thread enqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    cq.enque(i);
                    ThreadUtils.sleep(10);
                }
            }
        });
        final Thread deqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    final int elem = cq.deque();
                    System.out.println(elem);
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
