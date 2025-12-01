package info.jab.aoc.day15;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day15Test {

    @Test
    void should_solve_day15_part1_sample() {
        //Given
        String fileName = "/day15/day15-input-sample.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart1Result(fileName, false);

        //Then
        then(result).isEqualTo(2028);
    }

    @Test
    void should_solve_day15_part1_sample2() {
        //Given
        String fileName = "/day15/day15-input-sample2.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart1Result(fileName, false);

        //Then
        then(result).isEqualTo(10092);
    }

    @Test
    void should_solve_day15_part1() {
        //Given
        String fileName = "/day15/day15-input.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart1Result(fileName, false);

        //Then
        then(result).isEqualTo(1465523);
    }

    @Test
    void should_solve_day15_part2_sample() {
        //Given
        String fileName = "/day15/day15-input-sample3.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart2Result(fileName, true);

        //Then
        then(result).isEqualTo(618);
    }

    @Test
    void should_solve_day15_part2() {
        //Given
        String fileName = "/day15/day15-input.txt";

        //When
        var day1 = new Day15();
        var result = day1.getPart2Result(fileName, true);

        //Then
        then(result).isEqualTo(1471049);
    }
}
