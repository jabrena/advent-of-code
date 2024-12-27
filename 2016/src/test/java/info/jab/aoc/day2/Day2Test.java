package info.jab.aoc.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

import org.junit.jupiter.api.Disabled;

class Day2Test {

    @Test
    void should_solve_day2_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day2/day2-input-sample.txt";

            //When
            var day = new Day2();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("1985");
        });
    }

    @Test
    void should_solve_day2_part1() {
        Timer.run(() -> {     
            //Given
            String fileName = "/day2/day2-input.txt";

            //When
            var day = new Day2();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo("84452");
        });
    }

    @Test
    void should_solve_day2_part2_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day2/day2-input-sample.txt";

            //When
            var day = new Day2();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("5DB3");
        });
    }

    @Test
    void should_solve_day2_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day2/day2-input.txt";

            //When
            var day = new Day2();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo("D65C3");
        });
    }
}
