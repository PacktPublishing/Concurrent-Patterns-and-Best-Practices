package com.concurrency.book.chapter04;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class WordCountUsingMyThreadPool {
    final static AtomicLong count = new AtomicLong();

    public static void main(String[] args) {
        MyThreadPool threadPool = new MyThreadPool(4, 20);
        Stream<String> lines = readLines("input.txt");

        lines.forEach(line -> {
            try {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        final String[] words = line.split(" ");
                        count.getAndAdd(words.length);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        lines.close();
        threadPool.stop();
        System.out.println("count = " + count);
    }

    private static Stream<String> readLines(String fileName) {
        Path path = null;
        Stream<String> lines = null;
        try {
            path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(fileName).toURI());
            lines = Files.lines(path);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

}
