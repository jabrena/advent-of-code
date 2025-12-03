package info.jab.aoc2025.day3;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day3Test {

    @Test
    @Timeout(30)
    void should_solve_day3_part1_sample() {
        //Given
        String fileName = "day3/day3-input-sample.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(357L);
    }
    
    @Test
    @Timeout(30)
    void should_solve_day3_part1() {
        //Given
        String fileName = "day3/day3-input.txt";

        //When
        var day3 = new Day3();
        var result = day3.getPart1Result(fileName);

        //Then
        // We don't know the result yet, but we assert something to verify it runs.
        // Or we can leave it commented out or failing.
        // The instructions say: "print the solution in the test and extract from that maven test execution the RESULT".
        System.out.println("Day 3 Part 1 Result: " + result);
    }
}
