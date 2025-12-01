package info.jab.aoc2015.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day1Test {

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day = new Day1();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(74);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day = new Day1();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1795);
    }

}
