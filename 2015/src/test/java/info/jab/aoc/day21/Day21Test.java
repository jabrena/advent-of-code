package info.jab.aoc.day21;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day21Test {

    @Test
    void should_solve_day21_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day21/day21-input.txt";

            //When
            var day = new Day21();
            var result = day.getPart1Result(fileName);
            System.out.println("Day 21 Part 1 result: " + result);

            //Then
            then(result).isEqualTo(78);
        });
    }

    @Test
    void should_solve_day21_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day21/day21-input.txt";

            //When
            var day = new Day21();
            var result = day.getPart2Result(fileName);
            System.out.println("Day 21 Part 2 result: " + result);

            //Then
            then(result).isEqualTo(148);
        });
    }
}