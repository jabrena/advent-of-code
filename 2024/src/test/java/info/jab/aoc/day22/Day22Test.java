package info.jab.aoc.day22;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day22Test {

    @Test
    void should_solve_day1_part1() {
        //Given
        String fileName = "/day22/day22-input-sample.txt";

        //When
        var day = new Day22();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(889779);
    }

}
