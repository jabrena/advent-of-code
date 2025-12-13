package info.jab.aoc2017.day12;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day12Test {

    @Test
    void should_solve_day12_part1() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        Day12 day12 = new Day12();
        var result = day12.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(380);
    }

    @Test
    void should_solve_day12_part2() {
        //Given
        String fileName = "/day12/day12-input.txt";

        //When
        Day12 day12 = new Day12();
        var result = day12.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(181);
    }

}
