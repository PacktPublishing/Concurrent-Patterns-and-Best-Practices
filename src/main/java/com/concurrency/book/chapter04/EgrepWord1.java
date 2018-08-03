package com.concurrency.book.chapter04;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EgrepWord1 {
    private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static class WordFinder extends RecursiveTask<List<String>> {

        final File file;
        final String word;

        private WordFinder(File file, String word) {
            this.file = file;
            this.word = word;
        }

        @Override
        protected List<String> compute() {
            if (file.isFile()) {
                return grepInFile(file, word);
            } else {
                final File[] children = file.listFiles();
                if (children != null) {
                    List<ForkJoinTask<List<String>>> tasks = Lists.newArrayList();
                    List<String> result = Lists.newArrayList();
                    for (final File child : children) {
                        if (child.isFile()) {
                            List<String> taskResult = grepInFile(child, word);
                            result.addAll(taskResult);
                        } else {
                            tasks.add(new WordFinder(child, word));
                        }
                    }
                    for (final ForkJoinTask<List<String>> task : invokeAll(tasks)) {
                        List<String> taskResult = task.join();
                        result.addAll(taskResult);
                    }
                    return result;
                }
                return Collections.emptyList();
            }
        }

        private List<String> grepInFile(File file, String word) {
            final Stream<String> lines = readLines(file);
            final List<String> result = lines
                    .filter(x -> x.contains(word))
                    .map(y -> file + ": " + y)
                    .collect(Collectors.toList());

            return result;
        }

    }

    public static void main(String[] args) throws URISyntaxException {
        final Path dir1 = getResourcePath("dir1");
        final List<String> result = forkJoinPool.invoke(new WordFinder(dir1.toFile(), "in"));

        for(String r: result) {
            System.out.println(r);
        }
    }

    private static Stream<String> readLines(File file) {
        Stream<String> lines = null;
        try {
            Path path = file.toPath();
            lines = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private static Path getResourcePath(String fileName) throws URISyntaxException {
        final Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(fileName).toURI());
        return path;
    }

}
