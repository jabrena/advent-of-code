package info.jab.aoc2017.day13;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day13Test {

    @Test
    void should_solve_day13_part1() {
        //Given
        String fileName = "/day13/day13-input.txt";

        //When
        Day13 day13 = new Day13();
        var result = day13.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1840);
    }

    @Test
    void should_solve_day13_part2() {
        //Given
        String fileName = "/day13/day13-input.txt";

        //When
        Day13 day13 = new Day13();
        var result = day13.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(3850260);
    }

}
