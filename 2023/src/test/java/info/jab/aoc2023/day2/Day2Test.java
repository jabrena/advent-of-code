package info.jab.aoc2023.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day2Test {

    @Test
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "day2/input-sample.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(8);
    }

    @Test
    void should_solve_day2_part1() {
        //Given
        String fileName = "day2/input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(2683);
    }

    @Test
    void should_solve_day2_part2_with_sample() {
        //Given
        String fileName = "day2/input-sample.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(2286);
    }

    @Test
    void should_solve_day2_part2() {
        //Given
        String fileName = "day2/input.txt";

        //When
        Day2 day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(49710);
    }
}
