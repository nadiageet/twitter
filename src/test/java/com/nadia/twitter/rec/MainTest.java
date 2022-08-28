package com.nadia.twitter.rec;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.nadia.twitter.rec.Main.maxScoreBowling;
import static org.assertj.core.api.Assertions.assertThat;

class MainTest {

    @Test
    void test1() {
        assertThat(maxScoreBowling(List.of(1))).isEqualTo(1);
    }

    @Test
    void test2() {
        assertThat(maxScoreBowling(List.of(5, 5))).isEqualTo(25);
    }

    @Test
    void test3() {
        assertThat(maxScoreBowling(List.of(1, 8, 9, 2))).isEqualTo(75);
    }

    @Test
    void test4() {
        assertThat(maxScoreBowling(List.of(-10, 2, 8, 9, 2))).isEqualTo(76);
    }
}