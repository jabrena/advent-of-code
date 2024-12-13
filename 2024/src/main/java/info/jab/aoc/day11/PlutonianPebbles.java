package info.jab.aoc.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interesting implementations
 * https://github.com/jscharpff/AoC2024/blob/main/src/challenges/day11/PlutonianPebbles.java
 * https://github.com/bertjan/advent-of-code-2024/blob/main/src/main/java/AoC2024Day11.java
 */
public class PlutonianPebbles {

    /**
     * The implementation may cause a Java heap space error due to the exponential growth of the stones list during the simulation, especially if the number of blinks (blinks) is high or if the input string contains many stones.
     * Here are the main reasons for the error:
     *
     * Exponential Growth:
     * For each blink, stones split or are replaced with larger numbers, leading to rapid growth of the stones list.
     * Even modest inputs can result in millions of stones after a few iterations.
     *
     * Memory Usage:
     * Each String object in Java occupies memory overhead, and handling millions of String objects will quickly exhaust the heap.
     *
     * No Optimization:
     * The blink method directly creates a new ArrayList for every transformation, which further increases memory usage.
     */
    public List<String> blink(List<String> stones) {
        List<String> newStones = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                newStones.add("1");
            } else if (stone.length() % 2 == 0) {
                // Split the stone into two halves
                int mid = stone.length() / 2;
                String leftHalf = stone.substring(0, mid);
                String rightHalf = stone.substring(mid);

                // Remove leading zeroes from each half and ensure non-empty result
                String left = removeLeadingZeroes(leftHalf);
                String right = removeLeadingZeroes(rightHalf);

                newStones.add(left.isEmpty() ? "0" : left);
                newStones.add(right.isEmpty() ? "0" : right);
            } else {
                // Replace with a new stone engraved with the number * 2024
                long number = Long.parseLong(stone);
                newStones.add(String.valueOf(number * 2024));
            }
        }

        return newStones;
    }

    private String removeLeadingZeroes(String s) {
        return s.replaceFirst("^0+", "");
    }

    // Second part, it is real kick in the ass. Well done AOC!

    Map<String,Long> cache = new HashMap<>();

    public long simulateBlinks(String line, int blinks) {
        long result = 0;
        for (long curStone: Arrays.stream(line.split(" ")).mapToLong(Long::valueOf).toArray()) {
            result += count(curStone, blinks, cache);
        }
        return result;
    }

    private long count(long stone, int iter, Map<String,Long> cache) {
        String key = stone+" "+iter, stoneStr = ""+stone;
        if (cache.containsKey(key)) return cache.get(key);
        if (iter == 0) return 1;
        long count = (stone == 0L) ? count(1L, iter-1, cache) : stoneStr.length() % 2 == 0
                ? count(Long.parseLong(stoneStr.substring(0, stoneStr.length() / 2)), iter-1, cache) +
                count(Long.parseLong((stoneStr).substring(stoneStr.length() / 2)), iter-1, cache)
                : count(stone * 2024, iter-1, cache);
        cache.put(key, count);
        return count;
    }
}

