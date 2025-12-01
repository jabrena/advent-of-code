package info.jab.aoc.day21;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day21Test {

    @Test
    void should_solve_day21_part1() {
        //Given
        String fileName = "/day21/day21-input.txt";

        //When
        var day = new Day21();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("aefgbcdh");
    }

    @Test
    void should_solve_day21_part2() {
        //Given
        String fileName = "/day21/day21-input.txt";

        //When
        var day = new Day21();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("egcdahbf");
    }

}
