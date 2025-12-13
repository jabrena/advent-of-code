package info.jab.aoc2025.day5;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class Day5Test {

    @Test
    void should_solve_day5_part1() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        var day5 = new Day5();
        var result = day5.getPart1Result(fileName);

        //Then
        then(result).isEqualTo(739L);
    }

    @Test
    void should_solve_day5_part2() {
        //Given
        String fileName = "/day5/day5-input.txt";

        //When
        var day5 = new Day5();
        var result = day5.getPart2Result(fileName);

        //Then
        then(result).isEqualTo(344486348901788L);
    }

    @Test
    void should_solve_day5_part1_with_sample() {
        //Given
        String fileName = "/day5/day5-sample-part1.txt";

        //When
        var day5 = new Day5();
        var result = day5.getPart1Result(fileName);

        //Then
        // IDs: 1, 5, 8, 11, 17, 32
        // Fresh IDs: 5 (in 3-5), 11 (in 10-14), 17 (in 16-20 and 12-18)
        then(result).isEqualTo(3L);
    }

    @Test
    void should_solve_day5_part2_with_sample() {
        //Given
        String fileName = "/day5/day5-sample-part2.txt";

        //When
        var day5 = new Day5();
        var result = day5.getPart2Result(fileName);

        //Then
        // Ranges: 3-5, 10-14, 16-20, 12-18 (matches problem statement example)
        // After merging overlapping and adjacent ranges:
        // - 10-14, 12-18, 16-20 merge to 10-20
        // Result: [3-5, 10-20]
        // Coverage: 3 + 11 = 14
        then(result).isEqualTo(14L);
    }
}
