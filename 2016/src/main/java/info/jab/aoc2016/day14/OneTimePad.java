package info.jab.aoc2016.day14;

import com.putoet.resources.ResourceLines;
import com.putoet.security.MD5;
import info.jab.aoc.Solver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

/**
 * Solver for Day 14: One-Time Pad
 * Finds keys in a one-time pad using MD5 hashes.
 */
public final class OneTimePad implements Solver<Integer> {

    private final Map<String, String> hashCache = new ConcurrentHashMap<>();
    private final Map<String, String> stretchedHashCache = new ConcurrentHashMap<>();

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return findNthKey(salt, 64);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return findNthKeyWithStretching(salt, 64);
    }

    public int findNthKey(final String salt, final int n) {
        return findNthKeyInternal(salt, n, false);
    }

    public int findNthKeyWithStretching(final String salt, final int n) {
        return findNthKeyInternal(salt, n, true);
    }

    private int findNthKeyInternal(final String salt, final int n, final boolean useStretching) {
        hashCache.clear();
        stretchedHashCache.clear();
        int keyCount = 0;
        int index = 0;

        while (keyCount < n) {
            String hash = useStretching ? getStretchedHash(salt, index) : getHash(salt, index);
            Character triplet = findTriplet(hash);

            if (triplet != null && hasQuintupletInNext1000(salt, index, triplet, useStretching)) {
                keyCount++;
                if (keyCount == n) {
                    return index;
                }
            }
            index++;
        }

        return -1; // Should not reach here
    }

    private String getHash(final String salt, final int index) {
        String key = salt + index;
        return hashCache.computeIfAbsent(key, k -> MD5.hash(k).toLowerCase());
    }

    private String getStretchedHash(final String salt, final int index) {
        String key = salt + index;
        return stretchedHashCache.computeIfAbsent(key, k -> {
            String hash = MD5.hash(k).toLowerCase();
            // Apply key stretching: 2016 additional MD5 hashes
            for (int i = 0; i < 2016; i++) {
                hash = MD5.hash(hash).toLowerCase();
            }
            return hash;
        });
    }

    private Character findTriplet(final String hash) {
        for (int i = 0; i < hash.length() - 2; i++) {
            char c = hash.charAt(i);
            if (hash.charAt(i + 1) == c && hash.charAt(i + 2) == c) {
                return c;
            }
        }
        return null;
    }

    private boolean hasQuintupletInNext1000(final String salt, final int startIndex, final char character, final boolean useStretching) {
        String quintuplet = String.valueOf(character).repeat(5);
        
        // Pre-compute hashes in parallel for the next 1000 indices
        return IntStream.range(startIndex + 1, startIndex + 1001)
            .parallel()
            .anyMatch(i -> {
                String hash = useStretching ? getStretchedHash(salt, i) : getHash(salt, i);
                return hash.contains(quintuplet);
            });
    }
}

