package info.jab.aoc.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class Day2Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_day2_part1() {
        //Given
        String fileName = "day2/day2-input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.solvePartOne(fileName);

        //Then
        then(result).isEqualTo(51833);
    }

}
