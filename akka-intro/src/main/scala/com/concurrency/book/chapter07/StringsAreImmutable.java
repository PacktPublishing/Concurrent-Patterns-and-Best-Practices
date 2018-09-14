package com.concurrency.book.chapter07;

import java.util.HashSet;
import java.util.Set;

public class StringsAreImmutable {
    public static void main(String[] args) {
        String s = "Hi friends!";
        s.toUpperCase();
        System.out.println(s);

        String s1 = s.toUpperCase();
        System.out.println(s1);

        String s2 = s1;
        String s3 = s1;
        String s4 = s1;

        Set set = new HashSet<String>();
        set.add(s);
        set.add(s1);
        set.add(s2);
        set.add(s3);

        System.out.println(set);
    }
}
