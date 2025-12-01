package info.jab.aoc.day8;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Day8Test {

    @Test
    void testTotalDifference() {
        String[] inputs = {
            "\"\"",
            "\"abc\"",
            "\"aaa\\\"aaa\"",
            "\"\\x27\""
        };
        StringLiteralCalculator calculator = new StringLiteralCalculator();
        var result = calculator.calculateTotalDifference(Arrays.asList(inputs));
        then(result).isEqualTo(12);
    }

    @Test
    void should_solve_day8_part1() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        var day = new Day8();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1350);
    }

    @Test
    void should_solve_day8_part2() {
        //Given
        String fileName = "/day8/day8-input.txt";

        //When
        var day = new Day8();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2085);
    }

}
