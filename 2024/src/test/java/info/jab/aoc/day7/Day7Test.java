package info.jab.aoc.day7;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day7Test {

    @Test
    void should_solve_day7_part1_with_sample() {
        //Given
        String fileName = "/day7/day7-input-sample.txt";

        //When
        var day = new Day7();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(3749);
    }

    @Test
    void should_solve_day7_part1() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day = new Day7();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1399219271639L);
    }

    @Test
    void should_solve_day7_part2_with_sample() {
        //Given
        String fileName = "/day7/day7-input-sample.txt";

        //When
        var day = new Day7();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(11387);
    }

    @Test
    void should_solve_day7_part2() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        var day = new Day7();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(275791737999003L);
    }

}
