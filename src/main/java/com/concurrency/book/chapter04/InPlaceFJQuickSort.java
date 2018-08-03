package com.concurrency.book.chapter04;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class InPlaceFJQuickSort {

    public static final int NELEMS = 100;

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Random r = new Random();

        int[] arr = new int[NELEMS];
        for (int i = 0; i < arr.length; i++) {
            int k = r.nextInt(NELEMS);
            arr[i] = k;
        }

        ForkJoinQuicksortAction forkJoinQuicksortAction = new ForkJoinQuicksortAction(arr,
                0, arr.length - 1);
        forkJoinPool.invoke(forkJoinQuicksortAction);
        System.out.println(Arrays.toString(arr));
    }
}

class ForkJoinQuicksortAction extends RecursiveAction {
    public static final int LIMIT = 10;

    int[] arr;
    int left;
    int right;

    public ForkJoinQuicksortAction(int[] arr) {
        this(arr, 0, arr.length - 1);
    }

    public ForkJoinQuicksortAction(int[] arr, int left, int right) {
        this.arr = arr;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (isItASmallArray()) {
            Arrays.sort(arr, left, right + 1);
        } else {
            int pivotIndex = partition(arr, left, right);
            ForkJoinQuicksortAction task1 = new ForkJoinQuicksortAction(arr, left,
                    pivotIndex - 1);
            ForkJoinQuicksortAction task2 = new ForkJoinQuicksortAction(arr, pivotIndex + 1,
                    right);
            task1.fork();
            task2.compute();
            task1.join();
        }
    }

    int partition(int[] a, int p, int r) {
        int i = p - 1;
        int x = a[r];
        for (int j = p; j < r; j++) {
            if (a[j] < x) {
                i++;
                swap(a, i, j);
            }
        }
        i++;
        swap(a, i, r);
        return i;
    }

    void swap(int[] a, int p, int r) {
        int t = a[p];
        a[p] = a[r];
        a[r] = t;
    }

    private boolean isItASmallArray() {
        return right - left <= LIMIT;
    }
}

