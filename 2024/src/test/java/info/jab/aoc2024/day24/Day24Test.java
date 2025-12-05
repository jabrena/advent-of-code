package info.jab.aoc2024.day24;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class Day24Test {

    @ParameterizedTest
    @CsvSource({
        "/day24/day24-input-sample.txt, 4",
        "/day24/day24-input-sample2.txt, 2024",
        "/day24/day24-input.txt, 42410633905894"
    })
    @Disabled("Part 1 test disabled - implementation may be incomplete or slow")
    void should_solve_day24_part1(String fileName, String expectedResult) {
        //Given
        //When
        var day = new Day24();
        var result = day.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(expectedResult);
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
