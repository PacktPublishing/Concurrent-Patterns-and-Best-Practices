package com.concurrency.book.chapter05;

import java.util.concurrent.atomic.AtomicStampedReference;

public class TryAtomicStampedReference {
    public static void main(String[] args) {
        String firstRef = "Reference Value 0";
        int stamp1 = 0;

        AtomicStampedReference<String> atomicStringReference =
                new AtomicStampedReference<String>(
                        firstRef, stamp1
                );

        String newRef = "Reference Value 1";
        int newStamp = stamp1 + 1;

        boolean r = atomicStringReference
                .compareAndSet(firstRef, newRef, stamp1, newStamp);
        System.out.println("r: " + r);

        r = atomicStringReference
                .compareAndSet(firstRef, "new string", newStamp, newStamp + 1);
        System.out.println("r: " + r);

        r = atomicStringReference
                .compareAndSet(newRef, "new string", stamp1, newStamp + 1);
        System.out.println("r: " + r);

        r = atomicStringReference
                .compareAndSet(newRef, "new string", newStamp, newStamp + 1);
        System.out.println("r: " + r);
    }
}
