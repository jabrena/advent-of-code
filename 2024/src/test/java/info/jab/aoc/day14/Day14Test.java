package info.jab.aoc.day14;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day14Test {

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day14/day14-input.txt";

        //When
        var day1 = new Day14();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(889779);
    }

}
