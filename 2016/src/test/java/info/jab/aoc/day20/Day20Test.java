package info.jab.aoc.day20;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day20Test {

    @Test
    void should_solve_day20_part1() {
        //Given
        String fileName = "/day20/day20-input.txt";

        //When
        var day = new Day20();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(14975795L);
    }

    @Test
    void should_solve_day20_part2() {
        //Given
        String fileName = "/day20/day20-input.txt";

        //When
        var day = new Day20();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(101L);
    }

}
