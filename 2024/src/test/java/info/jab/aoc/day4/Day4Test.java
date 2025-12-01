package info.jab.aoc.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day4Test {

    @Test
    void should_solve_day4_part1_with_sample2() {
        //Given
        String fileName = "/day4/day4-input-sample2.txt";

        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(18);
    }

    @Test
    void should_solve_day4_part1_with_sample3() {
        //Given
        String fileName = "/day4/day4-input-sample3.txt";

        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(18);
    }

    @Test
    void should_solve_day4_part1() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(2464);
    }

    @Test
    void should_solve_day4_part2_sample1() {
        //Given
        String fileName = "/day4/day4-part2-input-sample.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(9);
    }

    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1982);
    }

}
