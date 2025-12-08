package info.jab.aoc2025.day8;

import info.jab.aoc.Day;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class Day8Test {

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part1() {
        // Given
        Day<Long> day8 = new Day8();

        // When
        Long result = day8.getPart1Result("/day8/day8-input.txt");

        // Then
        assertThat(result).isEqualTo(164475L);
        System.out.println("Day 8 Part 1 Result: " + result);
    }

    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS)
    void should_solve_part2() {
        // Given
        Day<Long> day8 = new Day8();

        // When
        Long result = day8.getPart2Result("/day8/day8-input.txt");

        // Then
        assertThat(result).isNotNull();
        System.out.println("Day 8 Part 2 Result: " + result);
    }
}
