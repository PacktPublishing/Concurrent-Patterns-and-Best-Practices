package com.concurrency.book.chapter04;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExampleThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(new Runnable() {
            public void run() {
                System.out.println("Hey pool!");
            }
        });

        executorService.shutdown();
    }
}
