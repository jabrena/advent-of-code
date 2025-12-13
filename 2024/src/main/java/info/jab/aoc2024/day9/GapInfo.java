package info.jab.aoc2024.day9;

/**
 * Represents gap information for disk compaction.
 * Contains position, size, and gap ID for efficient gap management.
 */
record GapInfo(int position, int size, int gapId) { }
