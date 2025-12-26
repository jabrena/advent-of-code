package info.jab.aoc2025.day3;

import info.jab.aoc.Trampoline;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * Bank abstraction representing a line of digits.
 * Optimized using FastUtil IntArrayList for primitive collections
 * and direct loop iteration to avoid allocations.
 * Immutable data structure following functional programming principles.
 * Each line from the input is converted to a Bank object.
 *
 * @param digits The list of digits (immutable, using FastUtil IntArrayList)
 */
public record Bank(IntArrayList digits) {
    /**
     * Creates a Bank from a line string.
     * Pure factory method that transforms input to output without side effects.
     * Uses FastUtil IntArrayList to avoid boxing overhead.
     *
     * @param line The input line containing digits
     * @return A Bank object containing the digits
     */
    public static Bank from(final String line) {
        final IntArrayList digits = new IntArrayList(line.length());
        line.chars().forEach(ch -> digits.add(ch - '0'));
        return new Bank(digits);
    }

    /**
     * Checks if the bank has enough digits for the required length.
     * Pure function with no side effects.
     *
     * @param length The required length
     * @return true if the bank has enough digits
     */
    public boolean hasEnoughDigits(final int length) {
        return digits.size() >= length;
    }

    /**
     * Calculates the maximum joltage that can be formed from this bank
     * by selecting digits in order to form a number of the specified length.
     * This is a pure function that transforms input to output without side effects.
     * Uses a trampoline pattern to avoid stack overflow from deep recursion.
     *
     * @param length The required length of the resulting number
     * @return The maximum joltage value
     */
    public long getMaxJoltage(final int length) {
        return Trampoline.run(buildJoltageTrampoline(length, 0, 0L, 0));
    }

    /**
     * Trampoline-based helper function that builds the maximum joltage.
     * Pure function with no side effects. Uses trampoline pattern to convert
     * recursive calls into iterative loops, preventing stack overflow.
     *
     * @param length      The total required length
     * @param iteration   The current iteration (0-based)
     * @param accumulator The accumulated joltage value
     * @param startPos    The starting position for the next search
     * @return A Trampoline representing either the result (Done) or continuation (More)
     */
    private Trampoline<Long> buildJoltageTrampoline(final int length, final int iteration,
                                                    final long accumulator, final int startPos) {
        if (iteration >= length) {
            return new Trampoline.Done<>(accumulator);
        }

        final int remaining = length - iteration;
        final int endPos = digits.size() - remaining;
        final MaxDigitResult maxResult = findMaxDigitInRange(startPos, endPos);

        return new Trampoline.More<>(() -> buildJoltageTrampoline(
                length,
                iteration + 1,
                accumulator * 10 + maxResult.value(),
                maxResult.index() + 1
        ));
    }

    /**
     * Result record containing the maximum digit value and its index.
     * Immutable data structure for functional programming.
     *
     * @param value The maximum digit value
     * @param index The index where the maximum digit was found
     */
    private record MaxDigitResult(int value, int index) { }

    /**
     * Finds the maximum digit value and its first occurrence index in the specified range.
     * Optimized to use direct loop iteration instead of creating intermediate collections.
     * Pure function with no side effects. Avoids allocations by iterating directly over digits.
     *
     * @param startPos The starting position (inclusive)
     * @param endPos   The ending position (inclusive)
     * @return A record containing the maximum digit value and its index
     */
    private MaxDigitResult findMaxDigitInRange(final int startPos, final int endPos) {
        int maxValue = -1;
        int maxIndex = startPos;

        // Direct iteration avoids creating intermediate collections
        for (int i = startPos; i <= endPos; i++) {
            final int value = digits.getInt(i);
            if (value > maxValue) {
                maxValue = value;
                maxIndex = i;
            }
        }

        return new MaxDigitResult(maxValue, maxIndex);
    }
}
