package com.concurrency.book.chapter04;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class WordCount1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Stream<String> lines = readLines("input.txt");
        List<Callable<Integer>> callables = new ArrayList<>();

        lines.forEach(line -> callables.add(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                final String[] words = line.split(" ");
                return words.length;
            }

        }));
        lines.close();

        List<Future<Integer>> futures = executorService.invokeAll(callables);

        for(Future<Integer> future : futures){
            System.out.println("future.get = " + future.get());
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
