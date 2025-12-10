package info.jab.aoc2025.day10;

import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

public final class Part1Solver {

    public long solve(Part1Problem problem) {
        return findMinPressesMitM(problem.target(), problem.buttons());
    }

    private long findMinPressesMitM(long target, long[] buttons) {
        int n = buttons.length;
        int mid = n / 2;
        int leftSize = mid;
        int rightSize = n - mid;

        // Meet-in-the-middle: Generate all sums for left half
        // Initialize with capacity to avoid resizing
        LongIntHashMap leftMap = new LongIntHashMap(1 << leftSize);
        long leftLimit = 1L << leftSize;

        long current = 0;

        // 0 case
        leftMap.put(0L, 0);

        for (long i = 1; i < leftLimit; i++) {
            int bit = Long.numberOfTrailingZeros(i);
            current ^= buttons[bit];

            long gray = i ^ (i >> 1);
            int presses = Long.bitCount(gray);

            int existing = leftMap.getIfAbsent(current, Integer.MAX_VALUE);
            if (presses < existing) {
                leftMap.put(current, presses);
            }
        }

        long minPresses = Long.MAX_VALUE;
        long rightLimit = 1L << rightSize;

        // Check right half combinations
        current = 0;

        // Check 0 case for right side (combining with all left side results)
        if (leftMap.containsKey(target)) {
            minPresses = leftMap.get(target);
        }

        for (long i = 1; i < rightLimit; i++) {
            int bit = Long.numberOfTrailingZeros(i);
            // Right side buttons start at index 'mid' (or 'leftSize')
            current ^= buttons[leftSize + bit];

            long needed = target ^ current;
            if (leftMap.containsKey(needed)) {
                // Only calculate bitCount if we have a match
                long gray = i ^ (i >> 1);
                int presses = Long.bitCount(gray);

                int totalPresses = presses + leftMap.get(needed);
                if (totalPresses < minPresses) {
                    minPresses = totalPresses;
                }
            }
        }

        return minPresses == Long.MAX_VALUE ? 0 : minPresses;
    }
}
