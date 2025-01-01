package info.jab.aoc.day9;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.putoet.utils.Timer;

class Day9Test {

    @Test
    void should_solve_day9_part1_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day9/day9-input-sample.txt";

            //When
            var day = new Day9();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(605);
        });
    }

    @Test
    void should_solve_day9_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day9/day9-input.txt";

            //When
            var day = new Day9();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(117);
        });
    }

    @Disabled
    @Test
    void should_solve_day9_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day9();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(2085);
        });
    }

}
