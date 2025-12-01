package info.jab.aoc2023.day9;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

public class Day9Test {

    @Disabled
    @Test
    void should_solve_day9_part1_with_sample() {
        //Given
        String fileName = "day9/input-sample.txt";

        //When
        var day = new Day9();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(13);
    }

}
