package com.concurrency.book.chapter05;

import java.util.concurrent.atomic.AtomicReference;

public class TryAtomicReference {
    public static void main(String[] args) {
        String firstRef = "Reference Value 0";

        AtomicReference<String> atomicStringReference =
                new AtomicReference<String>(firstRef);

        System.out.println(atomicStringReference.compareAndSet(firstRef, "Reference Value 1"));
        System.out.println(atomicStringReference.compareAndSet(firstRef, "Reference Value 2"));

        System.out.println(atomicStringReference.get());
    }
}
