package info.jab.aoc2018.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day2Test {

    @Test
    @Timeout(30)
    void should_solve_day2_part1() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.solvePartOne(fileName);

        //Then
        then(result).isEqualTo("7533");
    }

    @Test
    @Timeout(30)
    void should_solve_day2_part2() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.solvePartTwo(fileName);

        //Then
        then(result).isEqualTo("mphcuasvrnjzzkbgdtqeoylva");
    }

}
