package info.jab.aoc2016.day4;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void should_solve_day4_part1() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(409147);
    }
    
    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "/day4/day4-input.txt";

        //When
        var day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(991);
    }
}