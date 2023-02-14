package com.nadia.twitter.quizz;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExamplePolymorphisme {

    public static void main(String[] args) {
//        {
//            TimeGiver whenIsIt = new WhenIsIt();
//            String time = whenIsIt.getTime();
//            System.out.println("time ISO      = " + time);
//        }
//        {
//            TimeGiver whatHourIsIt = new WhatHourIsIt();
//            String time = whatHourIsIt.getTime();
//            System.out.println("hours/minutes = " + time);
//        }

        PrintTime printTime = new PrintTime(new WhenIsIt());

        printTime.printTime();

        ADecorator a = new ADecorator(new ADecorator2(new A()));

    }

}

class PrintTime {

    TimeGiver time;

    PrintTime(TimeGiver time) {
        this.time = time;
    }

    void printTime() {
        System.out.println(time.getTime());
    }
}

interface TimeGiver {

    String getTime();
}

class WhenIsIt implements TimeGiver {
    @Override
    public String getTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}

class WhatHourIsIt extends WhenIsIt {

    private int i; // Encapsulation


    private static final DateTimeFormatter HOURS_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String getTime() {
        return LocalDateTime.now().format(HOURS_FORMATTER);
    }
}


class A {
    public A() {
    }

    public A(A a) {

    }

}

class ADecorator extends A {

    public ADecorator(A a) {
        super(a);
    }
}

class ADecorator2 extends A {

    public ADecorator2(A a) {
        super(a);
    }
}

