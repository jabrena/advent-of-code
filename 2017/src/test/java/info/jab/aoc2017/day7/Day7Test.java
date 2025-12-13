package info.jab.aoc2017.day7;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day7Test {

    @Test
    void should_solve_day7_part1() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("ahnofa");
    }

    @Test
    void should_solve_day7_part2() {
        //Given
        String fileName = "/day7/day7-input.txt";

        //When
        Day7 day7 = new Day7();
        var result = day7.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("802");
    }

}
