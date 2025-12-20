package info.jab.aoc2025.day5;

import java.util.List;
import java.util.stream.Stream;

/**
 * Immutable state record for functional range merging.
 * Encapsulates the current merged range, accumulated results, and current index.
 * Used in functional iteration for merging overlapping and adjacent ranges.
 */
record MergeState(RangeData current, List<RangeData> merged, int index) {
    /**
     * Checks if there are more ranges to process.
     *
     * @param sortedRanges The sorted list of ranges being processed
     * @return true if there are more ranges to process, false otherwise
     */
    boolean hasNext(final List<RangeData> sortedRanges) {
        return index + 1 < sortedRanges.size();
    }

    /**
     * Processes the next range, either merging or adding to results.
     * Pure function returning new immutable state.
     *
     * @param sortedRanges The sorted list of ranges being processed
     * @return A new MergeState with the updated state
     */
    MergeState next(final List<RangeData> sortedRanges) {
        final RangeData next = sortedRanges.get(index + 1);
        if (current.overlapsOrAdjacent(next)) {
            return new MergeState(current.merge(next), merged, index + 1);
        } else {
            return new MergeState(next, Stream.concat(merged.stream(), Stream.of(current))
                    .toList(), index + 1);
        }
    }

    /**
     * Completes the merge by adding the last current range.
     *
     * @return The final immutable list of merged ranges
     */
    List<RangeData> complete() {
        return Stream.concat(merged.stream(), Stream.of(current))
                .toList();
    }
}

