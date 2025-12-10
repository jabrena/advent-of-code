package info.jab.aoc2025.day10;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Day10Test {

    @Test
    void should_solve_day10_part1() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart1Result("day10/day10-input-sample.txt");

        // Then
        assertThat(result).isEqualTo(7L);
    }
    
    @Test
    void should_solve_day10_part1_input() {
        // Given
        var day10 = new Day10();

        // When
        var result = day10.getPart1Result("day10/day10-input.txt");

        // Then
        assertThat(result).isNotNull();
    }
}
