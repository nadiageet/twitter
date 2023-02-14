package com.nadia.twitter.quizz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorldlineCodingGame {


    public static int getCoeff(String operation, int slug) {
        String sub = operation.substring(0, operation.length() - slug);
        System.out.println(sub);
        if (sub.isEmpty()) {
            return 1;
        } else if (sub.equals("-")) {
            return -1;
        } else {
            return Integer.parseInt(sub);
        }
    }

    public static List<String> filterWords(String[] words, String letters) {
        Set<Character> letterSet = new HashSet<>();
        for (int i = 0; i < letters.length(); i++) {
            letterSet.add(letters.charAt(i));
        }
        List<String> answers = new ArrayList<>();
        for (String word : words) {
            for (int j = 0; j < word.length(); j++) {
                char c = word.charAt(j);
                if (letterSet.contains(c)) {
                    answers.add(word);
                    break;
                }
            }
        }

        return answers;
        
    /*    Set<Character> letterSet = letters.chars()
                .mapToObj(value -> (char) value)
        .collect(Collectors.toSet());*/

//
//        return Arrays.stream(words)
//                .filter(word -> word.chars().mapToObj(value -> (char) value).anyMatch(letterSet::contains))
//                .toList();
    }


    public static void main(String[] args) {

        String[] words = {"the animals", "dog", "got", "a", "bone"};
        String letters = "ae";
        List<String> a = filterWords(words, letters);
        System.out.println("a = " + a);
      

   /*     String s = "g(x)=998x²+999x+998";
        s = s.replaceAll("-", "+-");
        int num = 20;

        String rhs = s.split("=")[1];
        System.out.println(rhs);

        int x2 = 0, x = 0, c = 0;

        for (String operation : rhs.split("\\+")) {
            if (operation.isEmpty()) {
                continue;
            }
            switch (String.valueOf(operation.charAt(operation.length() - 1))) {
                case "²": {
                    x2 = getCoeff(operation, 2);
                    break;
                }
                case "x": {
                    x = getCoeff(operation, 1);
                    break;
                }
                default: {
                    c = Integer.parseInt(operation);
                    break;
                }
            }
        }

        int answer = x2 * (num * num) + x * num + c;

        System.out.println(answer);*/

    }
}
