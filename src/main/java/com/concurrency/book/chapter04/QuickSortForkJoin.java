package com.concurrency.book.chapter04;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class QuickSortForkJoin {

    public static final int NELEMS = 1000;

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Random r = new Random();

        int[] arr = new int[NELEMS];
        for (int i = 0; i < arr.length; i++) {
            int k = r.nextInt(NELEMS);
            arr[i] = k;
        }

        ForkJoinQuicksortTask forkJoinQuicksortTask = new ForkJoinQuicksortTask(arr,
                0, arr.length - 1);
        final int[] result = forkJoinPool.invoke(forkJoinQuicksortTask);
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(result));
    }
}

class ForkJoinQuicksortTask extends RecursiveTask<int[]> {
    public static final int LIMIT = 100;

    int[] arr;
    int left;
    int right;

    public ForkJoinQuicksortTask(int[] arr) {
        this(arr, 0, arr.length - 1);
    }

    public ForkJoinQuicksortTask(int[] arr, int left, int right) {
        this.arr = arr;
        this.left = left;
        this.right = right;
    }

    @Override
    protected int[] compute() {
        if (isItASmallArray()) {
            Arrays.sort(arr, left, right + 1);
            return arr;
        } else {
            List<ForkJoinTask<int[]>> tasks = Lists.newArrayList();
            int pivotIndex = partition(arr, left, right);

            int[] arr0 = Arrays.copyOfRange(arr, left, pivotIndex);
            int[] arr1 = Arrays.copyOfRange(arr, pivotIndex + 1, right + 1);

            tasks.add(new ForkJoinQuicksortTask(arr0));
            tasks.add(new ForkJoinQuicksortTask(arr1));

            int[] result = new int[]{arr[pivotIndex]};
            boolean pivotElemCopied = false;
            for (final ForkJoinTask<int[]> task : invokeAll(tasks)) {
                int[] taskResult = task.join();
                if (!pivotElemCopied) {
                    result = Ints.concat(taskResult, result);
                    pivotElemCopied = true;
                } else {
                    result = Ints.concat(result, taskResult);
                }
            }
            return result;
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
