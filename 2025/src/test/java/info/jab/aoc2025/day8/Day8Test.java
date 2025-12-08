package info.jab.aoc2025.day8;

import info.jab.aoc.Day2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part1() {
        // Given
        Day2<Long, String, Integer> day8 = new Day8();

        // When
        Long result = day8.getPart1Result("/day8/day8-input.txt", 1000);

        // Then
        assertThat(result).isEqualTo(164475L);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part2() {
        // Given
        Day2<Long, String, Integer> day8 = new Day8();

        // When
        Long result = day8.getPart2Result("/day8/day8-input.txt", 1000);

        // Then
        assertThat(result).isEqualTo(169521198L);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part1_with_sample_after_10_connections() {
        // Given
        // According to problem description: https://adventofcode.com/2025/day/8
        // After making the ten shortest connections, there are 11 circuits:
        // one circuit which contains 5 junction boxes, one circuit which contains 4 junction boxes,
        // two circuits which contain 2 junction boxes each, and seven circuits which each contain
        // a single junction box. Multiplying together the sizes of the three largest circuits
        // (5, 4, and one of the circuits of size 2) produces 40.
        Day2<Long, String, Integer> day8 = new Day8();

        // When
        Long result = day8.getPart1Result("/day8/day8-input-sample.txt", 10);

        // Then
        assertThat(result).isEqualTo(40L);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part2_with_sample() {
        // Given
        Day2<Long, String, Integer> day8 = new Day8();

        // When
        Long result = day8.getPart2Result("/day8/day8-input-sample.txt", 1000);

        // Then
        // Part 2: Find the connection that merges all points into one cluster
        // The last connection that completes the merge is between points at indices 10 and 12
        // Point 10: (216, 146, 977) - x = 216
        // Point 12: (117, 168, 530) - x = 117
        // Result: 216 * 117 = 25272
        assertThat(result).isEqualTo(25272L);
    }
}
