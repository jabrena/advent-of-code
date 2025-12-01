package info.jab.aoc2016.day14;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import com.putoet.security.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2016/day/14
 * 
 * Day 14: One-Time Pad
 * 
 * Find the 64th key in a one-time pad using MD5 hashes.
 * A hash is a key if:
 * 1. It contains three of the same character in a row
 * 2. One of the next 1000 hashes contains that same character five times in a row
 */
public class Day14 implements Day<Integer> {

    private final Map<String, String> hashCache = new HashMap<>();
    private final Map<String, String> stretchedHashCache = new HashMap<>();

    public int findNthKey(String salt, int n) {
        return findNthKeyInternal(salt, n, false);
    }

    public int findNthKeyWithStretching(String salt, int n) {
        return findNthKeyInternal(salt, n, true);
    }

    private int findNthKeyInternal(String salt, int n, boolean useStretching) {
        hashCache.clear();
        stretchedHashCache.clear();
        int keyCount = 0;
        int index = 0;

        while (keyCount < n) {
            String hash = useStretching ? getStretchedHash(salt, index) : getHash(salt, index);
            Character triplet = findTriplet(hash);

            if (triplet != null) {
                // Check if any of the next 1000 hashes contain five of this character
                if (hasQuintupletInNext1000(salt, index, triplet, useStretching)) {
                    keyCount++;
                    if (keyCount == n) {
                        return index;
                    }
                }
            }
            index++;
        }

        return -1; // Should not reach here
    }

    private String getHash(String salt, int index) {
        String key = salt + index;
        return hashCache.computeIfAbsent(key, k -> MD5.hash(k).toLowerCase());
    }

    private String getStretchedHash(String salt, int index) {
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

    private Character findTriplet(String hash) {
        for (int i = 0; i < hash.length() - 2; i++) {
            char c = hash.charAt(i);
            if (hash.charAt(i + 1) == c && hash.charAt(i + 2) == c) {
                return c;
            }
        }
        return null;
    }

    private boolean hasQuintupletInNext1000(String salt, int startIndex, char character, boolean useStretching) {
        String quintuplet = String.valueOf(character).repeat(5);
        
        for (int i = startIndex + 1; i <= startIndex + 1000; i++) {
            String hash = useStretching ? getStretchedHash(salt, i) : getHash(salt, i);
            if (hash.contains(quintuplet)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return findNthKey(salt, 64);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return findNthKeyWithStretching(salt, 64);
    }
}