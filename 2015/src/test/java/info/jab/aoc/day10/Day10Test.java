package info.jab.aoc.day10;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day9/day9-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(492982);
        });
    }

    @Test
    void should_solve_day9_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day10();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(6989950);
        });
    }

}
