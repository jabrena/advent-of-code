package info.jab.aoc2025.day12;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class Day12Test {

    @Test
    void should_solve_part1_sample() {
        // Given
        var day12 = new Day12();
        var input = "day12/day12-input-sample.txt";

        // When
        var result = day12.getPart1Result(input);

        // Then
        assertThat(result).isEqualTo(2L);
    }
    
    @Test
    void should_solve_part1() {
        // Given
        var day12 = new Day12();
        var input = "day12/day12-input.txt";

        // When
        var result = day12.getPart1Result(input);

        // Then
        assertThat(result).isGreaterThan(0L);
    }
}
