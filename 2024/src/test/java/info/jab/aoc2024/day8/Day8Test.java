package info.jab.aoc2024.day8;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.resources.ResourceLines;

class Day8Test {

    @Test
    void should_solve_day8_part1_with_sample() {
        //Given
        String fileName = "/day8/day8-input-sample.txt";

        //When
        var day = new Day8();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(14);
    }

    @Test
    void should_solve_day8_part1() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        var day = new Day8();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(252);
    }

    @Test
    void should_solve_day8_part2_with_sample() {
        //Given
        String fileName = "/day8/day8-input-sample.txt";

        //When
        var day = new Day8();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(34);
    }

    @Test
    void should_solve_day8_part2() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        var day = new Day8();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(839);
    }

}
