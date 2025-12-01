package info.jab.aoc2024.day24;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

class Day24Test {

    @Test
    void should_solve_day24_part1_sample() {
        //Given
        String fileName = "/day24/day24-input-sample.txt";

        //When
        var day = new Day24();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("4");
    }

    @Test
    void should_solve_day24_part1_sample2() {
        //Given
        String fileName = "/day24/day24-input-sample2.txt";

        //When
        var day = new Day24();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("2024");
    }

    @Disabled
    @Test
    void should_solve_day24_part1() {
        //Given
        String fileName = "/day24/day24-input.txt";

        //When
        var day = new Day24();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo("42410633905894");
    }

    @Test
    void should_solve_day24_part2_sample() {
        //Given
        String fileName = "/day24/day24-input-sample3.txt";

        //When
        var day = new Day24();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("z00,z01,z02,z03,z04,z05");
    }

    @Test
    void should_solve_day24_part2() {
        //Given
        String fileName = "/day24/day24-input.txt";

        //When
        var day = new Day24();
        var result = day.getPart2Result(fileName);

        //Then
        then(result).isEqualTo("cqm,mps,vcv,vjv,vwp,z13,z19,z25");
    }
}
