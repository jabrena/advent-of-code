package info.jab.aoc2024.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day2Test {

    @Test
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day = new Day2();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(2);
    }

    @Test
    void should_solve_day2_part1() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day = new Day2();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(383);
    }

    /**
[7, 6, 4, 2, 1]
Ok
[1, 2, 7, 8, 9]
[2, 7, 8, 9]
[1, 7, 8, 9]
[1, 2, 8, 9]
[1, 2, 7, 9]
[1, 2, 7, 8]
[9, 7, 6, 2, 1]
[7, 6, 2, 1]
[9, 6, 2, 1]
[9, 7, 2, 1]
[9, 7, 6, 1]
[9, 7, 6, 2]
[1, 3, 2, 4, 5]
[3, 2, 4, 5]
[1, 2, 4, 5]
Ok
[8, 6, 4, 4, 1]
[6, 4, 4, 1]
[8, 4, 4, 1]
[8, 6, 4, 1]
Ok
[1, 3, 6, 7, 9]
Ok
     */
    @Test
    void should_solve_day2_part2_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day = new Day2();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(4);
    }

    @Test
    void should_solve_day2_part2() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day = new Day2();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(436);
    }
}
