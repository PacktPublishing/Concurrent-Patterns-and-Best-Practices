package com.concurrency.book.chapter05;

import com.concurrency.book.Utils.ThreadUtils;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<T> {
    AtomicReference<Node<T>> top = new AtomicReference<Node<T>>();

    public void push(T item) {
        Node<T> newHead = new Node<T>(item);
        Node<T> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.item;
    }

    private static class Node<E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }

    public static void main(String[] args) {
        final LockFreeStack<Integer> stack = new LockFreeStack<>();

        final Thread pushThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    stack.push(i);
                    ThreadUtils.sleep(10);
                }
            }
        });
        final Thread popThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; ++i) {
                    final Integer elem = stack.pop();
                    System.out.println("popped " + elem);
                    ThreadUtils.sleep(10);
                }
            }
        });

        pushThread.start();
        ThreadUtils.sleep(5);
        popThread.start();
    }
}
