package info.jab.aoc2023.day3;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day3Test {

    @Test
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "day3/input-sample.txt";

        //When
        Day3 day = new Day3();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(4361);
    }

    @Test
    void should_solve_day2_part1() {
        //Given
        String fileName = "day3/input.txt";

        //When
        Day3 day = new Day3();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(521601);
    }

    @Test
    void should_solve_day2_part2_with_sample() {
        //Given
        String fileName = "day3/input-sample.txt";

        //When
        Day3 day = new Day3();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(467835);
    }

    @Test
    void should_solve_day2_part2() {
        //Given
        String fileName = "day3/input.txt";

        //When
        Day3 day = new Day3();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(80694070);
    }

}
