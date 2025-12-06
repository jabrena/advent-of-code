package info.jab.aoc2015.day14;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    @Test
    void testPart1WithActualInput() {
        Day14 day14 = new Day14();
        int result = day14.getPart1Result("/day14/day14-input.txt");
        // This should be the actual result for part 1
        assertThat(result).isGreaterThan(0);
    }

    @Test
    void testPart2WithActualInput() {
        Day14 day14 = new Day14();
        int result = day14.getPart2Result("/day14/day14-input.txt");
        // This should be the actual result for part 2
        assertThat(result).isGreaterThan(0);
    }
}
