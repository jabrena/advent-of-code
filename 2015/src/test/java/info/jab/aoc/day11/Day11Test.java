package info.jab.aoc.day11;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class Day11Test {

    @Test
    void should_solve_day11_part1() {
        //Given
        String input = "hepxcrrq";

        //When
        var day = new Day11();
        var result = day.getPart1Result(input);

        //Then
        then(result).isEqualTo("hepxxyzz");
    }

    @Test
    void should_solve_day11_part2() {
        //Given
        String input = "hepxcrrq";

        //When
        var day = new Day11();
        var result = day.getPart2Result(input);

        //Then
        then(result).isEqualTo("heqaabcc");
    }

}
