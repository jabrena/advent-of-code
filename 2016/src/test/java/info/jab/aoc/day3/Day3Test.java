package info.jab.aoc.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day3Test {

    @Test
    void should_solve_day3_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "day3/day3-input.txt";

            //When
            var day = new Day3();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(1050);
        });
    }

    @Test
    void should_solve_day3_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "day3/day3-input.txt";

            //When
            var day = new Day3();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(1921);
        });
    }

}