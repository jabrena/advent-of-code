package info.jab.aoc2016.day12;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class Day12Test {

    @Test
    void should_solve_day12_part1() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(318007);
    }

    @Test
    void should_solve_day12_part2() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        var day = new Day12();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(9227661);
    }
}