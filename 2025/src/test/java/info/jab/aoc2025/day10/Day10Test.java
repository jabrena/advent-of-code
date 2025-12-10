package info.jab.aoc2025.day10;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    @Disabled("Part 2 is not implemented")
    @Test
    @Timeout(30)
    void should_solve_day10_part1() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart1Result("/day10/day10-input-sample.txt");

        // Then
        assertThat(result).isEqualTo(7L);
    }

    @Test
    @Timeout(30)
    void should_solve_day10_part1_input() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart1Result("/day10/day10-input.txt");

        // Then
        assertThat(result).isEqualTo(396L);
    }

    @Test
    @Timeout(30)
    void should_solve_day10_part2_sample() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart2Result("/day10/day10-input-sample.txt");

        // Then
        assertThat(result).isEqualTo(33L);
    }

    @Test
    @Timeout(30)
    void should_solve_day10_part2_input() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart2Result("/day10/day10-input.txt");

        // Then
        assertThat(result).isEqualTo(15688L);
    }
}
