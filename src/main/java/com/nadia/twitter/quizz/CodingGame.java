package com.nadia.twitter.quizz;

import java.util.Map;

public class CodingGame {

//    public static void main(String[] args) {
//
//        String[] array = {"Guigui", "Nadia", "Guigui", "Nadia", "Zorro"};
//
//        Map<String, Integer> map = new TreeMap<>();
//
//        int[] values = usingBuiltInMapApi(array, map);
//
////        System.out.println("values = " + Arrays.toString(values));
//
//        Map<String, Long> frq = Arrays.stream(array).collect(Collectors.groupingBy(
//                Function.identity(),
//                Collectors.counting()
//        ));
//
//        int[] result = frq.values().stream()
//                .mapToInt(Long::intValue)
//                .toArray();
//
//        System.out.println("result = " + Arrays.toString(result));
////        frq.entrySet().forEach(System.out::println);
//
//    }

    private static int[] usingBuiltInMapApi(String[] array, Map<String, Integer> map) {
        for (String s : array) {
            map.computeIfPresent(s, (s1, integer) -> integer + 1);
            map.putIfAbsent(s, 1);
        }

//        map.entrySet().forEach(System.out::println);

        return map.values()
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }


    public static void main(String[] args) {

        System.out.println(luckyMoney(24, 4));
        System.out.println(luckyMoney(8, 1));
        System.out.println(luckyMoney(8, 2));
        System.out.println(luckyMoney(12, 2));
        System.out.println(luckyMoney(7, 2));
    }

    public static int luckyMoney(int money, int children) {

        int numberOf8 = 0;

        while (money >= 8 && children > 0) {
            children--;
            money -= 8;

            if (money <= 0 && children > 0) {
                break;
            }

            if (money == 4 && children == 1) {
                break;
            }
            numberOf8++;
        }


        return numberOf8;
    }
}
