package com.nadia.twitter.rec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * 1 2 3 4
     * 1 2 3
     * 1 2
     * 1
     *
     * @param args
     */

    // TRUST THE FUNCTION
    public static void printNumbers(int n) {
        // base case
        if (n == 0) {
            return;
        }
        for (int i = 1; i <= n; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        printNumbers(n - 1);
    }

    // TRUST THE FUNCTION
    public static void printNumbersReverse(int n) {

        // base case
        if (n == 0) {
            return;
        }
        printNumbersReverse(n - 1);
        for (int i = 1; i <= n; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }


    /**
     * 1 + 2 + 3 + ... + (n-1) + n
     */
    public static int sumUntilN(int n) {
        if (n == 1) {
            return 1;
        }
        return n + sumUntilN(n - 1);
    }

    /**
     * 1 2 3 4
     * 1 2 3
     * 1 2
     * 1
     * 1 2
     * 1 2 3
     * 1 2 3 4
     *
     * @param args
     */

    public static void print(int n) {
        if (n == 1) {
            System.out.println(1);
            return;
        }
        for (int i = 1; i <= n; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        print(n - 1);

        for (int i = 1; i <= n; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
    }

    // 1 1 2 3  5 8 13 21 34 ...
    // f4 = f3 + f2
    private static Map<Integer, Integer> memo = new HashMap<>();

    public static int fib(int n) {
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        if (n < 2) {
            return 1;
        }
        int result = fib(n - 1) + fib(n - 2);
        memo.put(n, result);

        return result;
    }

    /**
     * return maximum score
     * pin -> score of pin integer Z
     * 2 pins p1, p2 -> p1*p2
     * ne pas la faire tomber
     *
     * @param pins
     * @return
     */
    public static int maxScoreBowling(List<Integer> pins) {

        int[] bestSoFar = new int[pins.size() + 1];

        bestSoFar[pins.size()] = 0;

        for (int i = pins.size() - 1; i >= 0; i--) {
            int adding = pins.get(i) + bestSoFar[i + 1];

            int bestLocal;
            if (i + 2 < bestSoFar.length) {
                int multipling = (pins.get(i) * pins.get(i + 1)) + bestSoFar[i + 2];
                bestLocal = Math.max(adding, multipling);
            } else {
                bestLocal = adding;
            }

            bestSoFar[i] = Math.max(bestLocal, bestSoFar[i + 1]);
        }

        return bestSoFar[0];
    }

    public static void main(String[] args) throws InterruptedException {

        // valide pour un certain i
        // n -> n + 1
//        printNumbers(4);
//        System.out.println();
//        printNumbersReverse(4);

//        xcs
//
//        for (int i = 2; i < 100; i++) {
//
//            print(i);
//            TimeUnit.MILLISECONDS.sleep(200);
//        }

//        System.out.println(fib(75));

    }
}
