package info.jab.aoc2025.day5;

import module java.base;

/**
 * Immutable state record for functional range merging.
 * Encapsulates the current merged range, accumulated results, and current index.
 * Used in functional iteration for merging overlapping and adjacent ranges.
 */
record RangeMergeState(Interval current, List<Interval> merged, int index) {
    /**
     * Checks if there are more ranges to process.
     *
     * @param sortedIntervals The sorted list of intervals being processed
     * @return true if there are more ranges to process, false otherwise
     */
    boolean hasNext(final List<Interval> sortedIntervals) {
        return index + 1 < sortedIntervals.size();
    }

    /**
     * Processes the next range, either merging or adding to results.
     * Pure function returning new immutable state.
     *
     * @param sortedIntervals The sorted list of intervals being processed
     * @return A new RangeMergeState with the updated state
     */
    RangeMergeState next(final List<Interval> sortedIntervals) {
        final Interval next = sortedIntervals.get(index + 1);
        if (current.overlapsOrAdjacent(next)) {
            return new RangeMergeState(current.merge(next), merged, index + 1);
        } else {
            return new RangeMergeState(next, Stream.concat(merged.stream(), Stream.of(current)).toList(), index + 1);
        }
    }

    /**
     * Completes the merge by adding the last current range.
     *
     * @return The final immutable list of merged intervals
     */
    List<Interval> complete() {
        return Stream.concat(merged.stream(), Stream.of(current)).toList();
    }
}

