package info.jab.aoc.day12;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day12Test {

    @Test
    void should_solve_day12_part1_with_sample() {
        //Given
        String fileName = "/day12/day12-input-sample.txt";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1930);
    }

    @Test
    void should_solve_day12_part1_with_sample2() {
        //Given
        String fileName = "/day12/day12-input-sample2.txt";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(32);
    }

    @Test
    void should_solve_day11_part1() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1473276);
    }

    @Disabled
    @Test
    void should_solve_day12_part2_with_sample() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(901100);
    }
}
