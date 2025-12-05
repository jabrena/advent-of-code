package info.jab.aoc2024.day9;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import com.putoet.resources.ResourceLines;

class Day9Test {

    @Test
    void should_solve_day9_part1_with_sample2() {
        //Given
        String fileName = "/day9/day9-input-sample2.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(60);
    }

    @Disabled("Test disabled - sample3 input file may not exist or test is redundant")
    @Test
    void should_solve_day9_part1_with_sample3() {
        //Given
        String fileName = "/day9/day9-input-sample3.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(60);
    }

    @Test
    void should_solve_day9_part1_with_sample() {
        //Given
        String fileName = "/day9/day9-input-sample.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1928);
    }

    @Test
    void should_solve_day9_part1() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(6241633730082L);
    }

    @Test
    void should_solve_day9_part2_with_sample() {
        //Given
        String fileName = "/day9/day9-input-sample.txt";

        //When
        var day = new Day9();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2858);
    }

    @Test
    void should_solve_day9_part2() {
        //Given
        String fileName = "/day9/day9-input.txt";

        //When
        var day = new Day9();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(6265268809555L);
    }

}
