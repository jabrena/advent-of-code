package info.jab.aoc2025.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day4Test {

    @Test
    @Timeout(30)
    void should_solve_day4_part1() {
        //Given
        String fileName = "day4/day4-input.txt";

        //When
        var day4 = new Day4();
        var result = day4.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(0);
    }
}
