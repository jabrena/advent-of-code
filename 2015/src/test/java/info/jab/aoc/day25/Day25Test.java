package info.jab.aoc.day25;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day25Test {

    @Test
    void should_solve_day25_part1() {
        //Given
        String fileName = "day25/day25-input.txt";

        //When
        var day = new Day25();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(8997277L);
    }

}