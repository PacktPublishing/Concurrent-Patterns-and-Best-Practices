package com.concurrency.book.chapter02;

import java.util.HashMap;
import java.util.Map;

public class VarsThrSafety {
    private static int p;
    final int x = 0;
    int y;

    private void m(int i, int i1) {
        int k = 9;
        Map<String, Integer> map = new HashMap<>();
        // ...
    }
}
