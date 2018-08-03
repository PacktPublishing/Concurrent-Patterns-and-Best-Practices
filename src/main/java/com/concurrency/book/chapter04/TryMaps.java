package com.concurrency.book.chapter04;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TryMaps {
    private static Map<Character, Integer> mergeMaps(Map<Character, Integer> m1, Map<Character, Integer>
            m2) {
        final Map<Character, Integer> mergedMap = Stream.of(m1, m2).flatMap(m -> m.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> x + y));
        return mergedMap;
    }

    public static void main(String[] args) {
        Map<Character, Integer> m1 = new HashMap();

        m1.put('a', 1);
        m1.put('b', 2);
        m1.put('c', 3);

        Map<Character, Integer> m2 = new HashMap();

        m2.put('a', 1);
        m2.put('b', 2);
        m2.put('c', 3);

        List<Character> aToD = Arrays.asList('a', 'b', 'c', 'd');
        List<Character> eToG = Arrays.asList('e', 'f', 'g');
        Stream<List<Character>> stream = Stream.of(aToD, eToG);
        stream.flatMap(l -> l.stream())
                .peek(System.out::println);
//                .map(c -> (int)c)
//                .forEach(i -> System.out.format("%d ", i));

    }
}
