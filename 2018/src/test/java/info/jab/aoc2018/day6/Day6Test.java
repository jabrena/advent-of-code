package info.jab.aoc2018.day6;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day6Test {

    @Test
    @Timeout(30)
    void should_solve_day6_part1() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day6 = new Day6();
        var result = day6.solvePartOne(fileName);

        //Then
        then(result).isEqualTo(3006);
    }

    @Test
    @Timeout(30)
    void should_solve_day6_part2() {
        //Given
        String fileName = "/day6/day6-input.txt";

        //When
        var day6 = new Day6();
        var result = day6.solvePartTwo(fileName);

        //Then
        then(result).isEqualTo(42998);
    }

}
