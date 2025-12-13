package info.jab.aoc2025.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day1Test {

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day1 = new Day1();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1034);
    }

    @Test
    void should_solve_day1_part2() {
        //Given
        String fileName = "/day1/day1-input.txt";

        //When
        var day1 = new Day1();
        var result = day1.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(6166);
    }

}
