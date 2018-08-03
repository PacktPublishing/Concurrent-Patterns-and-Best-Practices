package com.concurrency.book.chapter04;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.concurrent.ForkJoinTask.invokeAll;

public class WordFreqCounter {
    private final static ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static class WordFinder extends RecursiveTask<Map<String, Integer>> {

        final String line;

        private WordFinder(String line) {
            this.line = line;
        }

        @Override
        protected Map<String, Integer> compute() {
            List<ForkJoinTask<Map<String, Integer>>> tasks =
                    new ArrayList<>();
            final String[] words = line.split(" ");
            if (words.length == 0) {
                return Collections.emptyMap();
            }
            Map<String, Integer> result = Maps.newHashMap();
            for (String w : words) {
                Integer val = result.get(w);
                if (val == null)
                    val = 0;
                result.put(w, val + 1);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        Stream<String> lines = readLines("input.txt");
        List<WordFinder> list = new ArrayList<>();
        lines.forEach(line ->
                list.add(new WordFinder(line)));
        Map<String, Integer> result = Collections.emptyMap();

        for (final ForkJoinTask<Map<String, Integer>> task : invokeAll(list)) {
            final Map<String, Integer> taskResult = task.join();
            result = mergeMaps(result, taskResult);
        }
        System.out.println(result);
    }

    private static Map<String, Integer> mergeMaps(Map<String, Integer> m1, Map<String, Integer>
            m2) {
        final Map<String, Integer> mergedMap = Stream.of(m1, m2).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x + y));
        return mergedMap;
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
