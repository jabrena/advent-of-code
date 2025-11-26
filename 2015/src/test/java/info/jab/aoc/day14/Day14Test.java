package info.jab.aoc.day14;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class Day14Test {

    private final String testInput = """
        Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
        Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
        """;

    @Test
    void testPart1WithExample() {
        Day14 day14 = new Day14();
        int result = day14.solve(testInput, 1000);
        assertThat(result).isEqualTo(1120);
    }

    @Test
    void testPart1WithActualInput() throws IOException {
        String input = Files.readString(Path.of("src/test/resources/day14/day14-input.txt"));
        Day14 day14 = new Day14();
        int result = day14.solve(input, 2503);
        // This should be the actual result for part 1
        assertThat(result).isGreaterThan(0);
        System.out.println("Part 1 result: " + result);
    }

    @Test
    void testPart2WithExample() {
        Day14 day14 = new Day14();
        int result = day14.solvePart2(testInput, 1000);
        assertThat(result).isEqualTo(689);
    }

    @Test
    void testPart2WithActualInput() throws IOException {
        String input = Files.readString(Path.of("src/test/resources/day14/day14-input.txt"));
        Day14 day14 = new Day14();
        int result = day14.solvePart2(input, 2503);
        // This should be the actual result for part 2
        assertThat(result).isGreaterThan(0);
        System.out.println("Part 2 result: " + result);
    }
}