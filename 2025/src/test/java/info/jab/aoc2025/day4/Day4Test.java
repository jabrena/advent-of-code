package info.jab.aoc2025.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void should_solve_day4_part1() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day4 = new Day4();
        var result = day4.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1370);
    }

    @Test
    void should_solve_day4_part2_with_sample() {
        //Given
        String fileName = "/day4/day4-part2-input-sample.txt";

        //When
        var day4 = new Day4();
        var result = day4.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(43);
    }

    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day4 = new Day4();
        var result = day4.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(8437);
    }
}
