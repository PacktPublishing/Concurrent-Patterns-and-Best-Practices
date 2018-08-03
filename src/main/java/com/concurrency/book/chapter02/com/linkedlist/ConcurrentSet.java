package com.concurrency.book.chapter02.com.linkedlist;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentSet {
    public ConcurrentSet() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    private class Node {
        int item;
        Node next;

        public Node(int i) {
            this.item = i;
        }
    }

    private boolean add(int i) {
        Node prev = null;
        Node curr = head;

        lck.lock();

        try {
            while (curr.item < i) {
                prev = curr;
                curr = curr.next;
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
            lck.unlock();
        }
    }

    private boolean remove(int i) {
        Node prev = null;
        Node curr = head;

        lck.lock();

        try {
            while (curr.item < i) {
                prev = curr;
                curr = curr.next;
            }
            if (curr.item == i) {
                prev.next = curr.next;
                return true;
            } else {
                return false;
            }
        } finally {
            lck.unlock();
        }
    }

    private boolean lookUp(int i) {
        Node prev = null;
        Node curr = head;

        lck.lock();

        try {
            while (curr.item < i) {
                prev = curr;
                curr = curr.next;
            }
            if (curr.item == i) {
                return true;
            }
            return false;
        } finally {
            lck.unlock();
        }
    }


    @Override
    public String toString() {
        lck.lock();

        try {
            String v = "";
            for (Node n = head.next; n.item != Integer.MAX_VALUE; n = n.next) {
                v += n.item + ",";
            }
            if (!v.isEmpty()) {
                v = v.substring(0, v.length() - 1);
            }
            return v;
        } finally {
            lck.unlock();
        }
    }

    private Node head;

    private Lock lck = new ReentrantLock();

    public static void main(String[] args) {
        ConcurrentSet list = new ConcurrentSet();

        list.add(9);
        list.add(1);
        list.add(1);
        list.add(9);
        list.add(12);
        list.add(12);

        System.out.println(list.lookUp(112));
    }
}
