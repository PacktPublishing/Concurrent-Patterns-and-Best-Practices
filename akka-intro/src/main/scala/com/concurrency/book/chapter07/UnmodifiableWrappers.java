package com.concurrency.book.chapter07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnmodifiableWrappers {

    public static List<Integer> createList(Integer... elems) {
        List<Integer> list = new ArrayList<>();
        for(Integer i : elems) {
            list.add(i);
        }
        return Collections.unmodifiableList(list);
    }

    public static void main(String[] args) {
        List<Integer> readOnlyList = createList(1, 2, 3);
        System.out.println(readOnlyList);
        readOnlyList.add(4);
    }
}
