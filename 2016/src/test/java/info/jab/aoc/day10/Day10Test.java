package info.jab.aoc.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day10/day10-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart1Result(fileName);

            //Then
            System.out.println("Day 10 Part 1 result: " + result);
            then(result).isEqualTo(157);
        });
    }
}