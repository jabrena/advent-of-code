package info.jab.aoc2015.day12;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day12Test {

    @Test
    void should_solve_day11_part1() {
        //Given
        String fileName = "/day12/day12-input.json";

        //When
        var day = new Day12();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(111754);
    }

    @Test
    void should_solve_day11_part2() {
        //Given
        String fileName = "/day12/day12-input.json";

        //When
        var day = new Day12();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(65402);
    }

}
