package info.jab.aoc2025.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day1Test {

    @Test
    @Timeout(30)
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day1 = new Day1();
        var result = day1.solvePartOne(fileName);

        //Then
        then(result).isEqualTo(1034);
    }

    @Test
    @Timeout(30)
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day1 = new Day1();
        var result = day1.solvePartTwo(fileName);

        //Then
        then(result).isEqualTo(6166);
    }

}
