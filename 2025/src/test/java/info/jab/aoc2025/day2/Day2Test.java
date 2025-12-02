package info.jab.aoc2025.day2;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class Day2Test {

    @Test
    @Timeout(30)
    void should_solve_day2_part1_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(1227775554L);
    }

    @Test
    @Timeout(30)
    void should_solve_day2_part1() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(21898734247L);
    }

    @Test
    @Timeout(30)
    void should_solve_day2_part2_with_sample() {
        //Given
        String fileName = "/day2/day2-input-sample.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(4174379265L);
    }

    @Test
    @Timeout(30)
    void should_solve_day2_part2() {
        //Given
        String fileName = "/day2/day2-input.txt";

        //When
        var day2 = new Day2();
        var result = day2.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(28915664389L);
    }

}
