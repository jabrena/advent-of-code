package info.jab.aoc.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day3Test {

    @Test
    @Timeout(30)
    void should_solve_day3_part1() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day3 = new Day3();
        var result = day3.solvePartOne(fileName);

        //Then
        then(result).isEqualTo(106501);
    }

    @Test
    @Timeout(30)
    void should_solve_day3_part2() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day3 = new Day3();
        var result = day3.solvePartTwo(fileName);

        //Then
        then(result).isEqualTo(632);
    }

}
