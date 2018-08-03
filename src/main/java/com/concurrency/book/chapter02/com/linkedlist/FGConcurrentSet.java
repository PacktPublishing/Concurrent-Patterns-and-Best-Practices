package com.concurrency.book.chapter02.com.linkedlist;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FGConcurrentSet {
    public FGConcurrentSet() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    private class Node {
        int item;
        Node next;
        private Lock lck = new ReentrantLock();

        private Node(int i) {
            this.item = i;
        }

        private void lock() {
            lck.lock();
        }

        private void unlock() {
            lck.unlock();
        }
    }

    private boolean add(int i) {
        head.lock();
        Node prev = head;

        try {
            Node curr = prev.next;

            curr.lock();

            try {
                while (curr.item < i) {
                    prev.unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.item == i) {
                    return false;
                } else {
                    Node node = new Node(i);
                    node.next = curr;
                    prev.next = node;
                    return true;
                }
            } finally {
                curr.unlock();
            }
        } finally {
            prev.unlock();
        }
    }

    private boolean remove(int i) {
        head.lock();
        Node prev = head;

        try {
            Node curr = prev.next;

            curr.lock();

            try {
                while (curr.item < i) {
                    prev.unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.item == i) {
                    prev.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            prev.unlock();
        }
    }

    private boolean lookUp(int i) {
        head.lock();
        Node prev = head;

        try {
            Node curr = prev.next;

            curr.lock();

            try {
                while (curr.item < i) {
                    prev.unlock();
                    prev = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.item == i) {
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            prev.unlock();
        }
    }

    private Node head;

    public static void main(String[] args) {
        FGConcurrentSet list = new FGConcurrentSet();

        list.add(9);
        list.add(1);
        list.add(1);
        list.add(9);
        list.add(12);
        list.add(12);

        System.out.println(list.lookUp(12));
        list.remove(12);
        System.out.println(list.lookUp(12));
        System.out.println(list.lookUp(9));

    }
}
