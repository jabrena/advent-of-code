package info.jab.aoc.day1;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day2Test {

    @Test
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day1 = new Day2();
        var result = day1.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(11);
    }

}
