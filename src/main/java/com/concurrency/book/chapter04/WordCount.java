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

public class WordCount {
    final static AtomicLong count = new AtomicLong();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Stream<String> lines = readLines("input.txt");
        lines.forEach(line -> executorService.execute(new Runnable() {
            @Override
            public void run() {
                final String[] words = line.split(" ");
                count.getAndAdd(words.length);
            }
        }));
        lines.close();
        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
