package info.jab.aoc2023.day4;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day4Test {

    @Test
    void should_solve_day4_part1_with_sample() {
        //Given
        String fileName = "day4/input-sample.txt";

        //When
        Day4 day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(13);
    }

    @Test
    void should_solve_day4_part1() {
        //Given
        String fileName = "day4/input.txt";

        //When
        Day4 day = new Day4();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(18619);
    }

    @Test
    void should_solve_day4_part2_with_sample() {
        //Given
        String fileName = "day4/input-sample2.txt";

        //When
        Day4 day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(30);
    }

    @Test
    void should_solve_day4_part2() {
        //Given
        String fileName = "day4/input.txt";

        //When
        Day4 day = new Day4();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(8063216);
    }
}
