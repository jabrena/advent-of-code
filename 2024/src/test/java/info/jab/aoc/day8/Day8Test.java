package info.jab.aoc.day8;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.resources.ResourceLines;
import com.putoet.utils.Timer;

class Day8Test {

    @Test
    void should_solve_day8_part1_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input-sample.txt";

            //When
            var day = new Day8();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(14);
        });
    }

    @Test
    void should_solve_day8_part1() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day8();
            var result = day.getPart1Result(fileName);

            //Then
            then(result).isEqualTo(252);
        });
    }

    @Test
    void should_solve_day8_part2_with_sample() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input-sample.txt";

            //When
            var day = new Day8();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(34);
        });
    }

    @Test
    void should_solve_day8_part2() {
        Timer.run(() -> {
            //Given
            String fileName = "/day8/day8-input.txt";

            //When
            var day = new Day8();
            var result = day.getPart2Result(fileName);

            //Then
            then(result).isEqualTo(839);
        });
    }

}
