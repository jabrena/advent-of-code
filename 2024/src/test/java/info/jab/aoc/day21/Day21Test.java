package info.jab.aoc.day21;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day21Test {

    @Test
    void should_solve_day21_part1() {
        //Given
        String fileName = "/day21/day21-input.txt";

        //When
        Day21 day1 = new Day21();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(889779);
    }

}
