package com.concurrency.book.chapter04;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;

import static java.util.concurrent.ForkJoinTask.invokeAll;

public class EgrepWord {
    private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

    public static void main(String[] args) {
        Stream<String> lines = readLines("input.txt");
        List<WordFinder> taskList = new ArrayList<>();
        lines.forEach(line ->
                taskList.add(new WordFinder(line, "is")));
        List<String> result = new ArrayList<>();

        for (final WordFinder task : invokeAll(taskList)) {
            final List<String> taskResult = (List<String>) task.join();
            result.addAll(taskResult);
        }
        for(String r: result) {
            System.out.println(r);
        }

    }

    private static class WordFinder extends RecursiveTask<List<String>> {

        final String line;
        final String word;

        private WordFinder(String line, String word) {
            this.line = line;
            this.word = word;
        }

        @Override
        protected List<String> compute() {
            List<ForkJoinTask<List<String>>> tasks =
                    new ArrayList<>();
            if (line.contains(word))
                return Lists.newArrayList(line);
            else
                return Collections.EMPTY_LIST;
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
