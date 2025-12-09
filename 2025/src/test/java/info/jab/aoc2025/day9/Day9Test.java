package info.jab.aoc2025.day9;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Day9Test {

    @Test
    void should_solve_day9_part1() {
        // Given
        var day9 = new Day9();

        // When
        var result = day9.getPart1Result("/day9/day9-input-sample.txt");

        // Then
        assertThat(result).isEqualTo(50L);
    }

    @Test
    void should_solve_day9_part1_real_input() {
        // Given
        var day9 = new Day9();

        // When
        var result = day9.getPart1Result("/day9/day9-input.txt");

        // Then
        assertThat(result).isEqualTo(4741848414L);
    }

    @Test
    void should_solve_day9_part2() {
        // Given
        var day9 = new Day9();

        // When
        var result = day9.getPart2Result("/day9/day9-input-sample.txt");

        // Then
        assertThat(result).isEqualTo(0L); // Placeholder
    }
}
