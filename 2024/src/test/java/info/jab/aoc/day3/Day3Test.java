package info.jab.aoc.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day3Test {

    @Disabled
    @Test
    void should_solve_day3_part1_with_sample() {
        //Given
        String fileName = "/day3/day3-input-sample.txt";

        //When
        var day = new Day3();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(161);
    }

    @Disabled
    @Test
    void should_solve_day3_part1() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day = new Day3();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(157621318);
    }

    @Test
    void should_solve_day3_part2_with_sample() {
        //Given
        String fileName = "/day3/day3-input-sample2.txt";

        //When
        var day = new Day3();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(48);
    }

    @Test
    void should_solve_day3_part2() {
        //Given
        String fileName = "/day3/day3-input.txt";

        //When
        var day = new Day3();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(79845780);
    }

}
