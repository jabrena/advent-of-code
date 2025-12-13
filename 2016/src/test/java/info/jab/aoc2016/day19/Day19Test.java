package info.jab.aoc2016.day19;

import static org.assertj.core.api.BDDAssertions.then;


import org.junit.jupiter.api.Test;

class Day19Test {

    @Test
    void should_solve_day19_part1() {
        //Given
        String fileName = "/day19/day19-input.txt";

        //When
        var day = new Day19();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1834471);
    }

    @Test
    void should_solve_day19_part2() {
        //Given
        String fileName = "/day19/day19-input.txt";

        //When
        var day = new Day19();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(1420064);
    }

}
