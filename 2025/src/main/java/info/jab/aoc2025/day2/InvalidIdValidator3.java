package info.jab.aoc2025.day2;

import module java.base;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public final class InvalidIdValidator3 implements Solver<Long> {

    /**
     * Pattern to match all digit sequences in the input (for parsing ranges).
     */
    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\d+)");

    /**
     * Pattern to match a digit sequence followed by itself (exactly 2 repeats).
     */
    private static final Pattern TWICE_PATTERN = Pattern.compile("(\\d+)(\\1)");

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part one.
     * An invalid ID must have 2 or fewer repeated parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartOne(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        return solve(input, 1);
    }

    /**
     * Calculates the sum of all invalid IDs in the given ranges for part two.
     * An invalid ID must have 99 or fewer repeated parts.
     *
     * @param fileName The input file name
     * @return The sum of all invalid IDs
     */
    @Override
    public Long solvePartTwo(final String fileName) {
        final String input = ResourceLines.line(fileName).trim();
        return solve(input, 2);
    }

    /**
     * Solves the invalid ID validation problem for the specified part.
     * Uses a stream-based approach:
     * - Direct regex parsing to extract all numbers
     * - Gatherers.windowFixed(2) to pair numbers into ranges
     * - flatMapToLong to generate all IDs from all ranges
     * - Direct filtering and summing in the stream pipeline
     *
     * @param input Input string containing comma-separated ranges
     * @param part Part number (1 or 2)
     * @return The sum of all invalid IDs
     */
    private Long solve(final String input, final int part) {
        final int maxRepeats = part == 1 ? 2 : 99;
        final LongToIntFunction repeatedFunction = part == 1
            ? this::repeatedOptimized
            : this::repeated;

        // Solution approach: extract all numbers with regex, pair them with windowFixed(2)
        return RANGE_PATTERN.matcher(input).results()
            .map(result -> Long.parseLong(result.group()))
            .gather(Gatherers.windowFixed(2))
            .flatMapToLong(list -> LongStream.rangeClosed(list.getFirst(), list.getLast()))
            .filter(id -> {
                final int repeats = repeatedFunction.applyAsInt(id);
                return repeats > 0 && repeats <= maxRepeats;
            })
            .sum();
    }

    /**
     * Optimized check for exactly 2 repeats.
     * Uses the pattern (\\d+)(\\1) which matches a chunk followed by itself.
     * This is more efficient than the general repeated() method for part 1.
     *
     * @param id The ID to check
     * @return 2 if the ID has exactly 2 repeats, 0 otherwise
     */
    private int repeatedOptimized(final long id) {
        final String idStr = String.valueOf(id);
        return TWICE_PATTERN.matcher(idStr).matches() ? 2 : 0;
    }

    /**
     * Calculates the number of times a chunk repeats to form the given ID string.
     * Returns 0 if the ID is not composed of repeated chunks.
     * Uses regex pattern matching.
     * The solution uses (\\d+)(\\1) to match exactly 2 repeats with .matches().
     * This method generalizes to count any number of repeats by checking if
     * the entire string matches a pattern where a chunk is repeated multiple times.
     * Uses Stream API to find the first matching chunk pattern.
     *
     * @param id The ID to check
     * @return Number of repeats (0 if not repeated)
     */
    private int repeated(final long id) {
        final String idStr = String.valueOf(id);
        final int length = idStr.length();
        final int halfLength = length / 2;

        // Try all possible chunk lengths from largest to smallest using Stream API
        return IntStream.iterate(halfLength, chunkLen -> chunkLen >= 1, chunkLen -> chunkLen - 1)
            .filter(chunkLen -> length % chunkLen == 0) // can't fill the whole string if not divisible
            .mapToObj(chunkLen -> checkRepeatedPattern(chunkLen, idStr, length))
            .flatMap(Optional::stream)
            .findFirst()
            .orElse(0);
    }

    /**
     * Checks if the ID string matches a repeated pattern for the given chunk length.
     * Creates a regex pattern where the chunk is repeated to form the entire string.
     *
     * @param chunkLen The length of the chunk to test
     * @param idStr The ID string to check
     * @param length The total length of the ID string
     * @return Optional containing the number of repeats if pattern matches, empty otherwise
     */
    private Optional<Integer> checkRepeatedPattern(final int chunkLen, final String idStr, final int length) {
        final int repeats = length / chunkLen;
        final String chunk = idStr.substring(0, chunkLen);
        final String pattern = "^(" + chunk + "){" + repeats + "}$";
        return Pattern.matches(pattern, idStr) ? Optional.of(repeats) : Optional.empty();
    }
}
