package info.jab.aoc2025.day3;

import info.jab.aoc.Day;
import info.jab.aoc.Utils;
import java.util.List;

public class Day3 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = Utils.readFileToList(fileName);
        long totalJoltage = 0;
        for (String line : lines) {
            totalJoltage += getMaxJoltage(line);
        }
        return totalJoltage;
    }

    private int getMaxJoltage(String line) {
        int maxJoltage = 0;
        int n = line.length();
        int[] digits = new int[n];
        for (int i = 0; i < n; i++) {
            digits[i] = line.charAt(i) - '0';
        }

        for (int i = 0; i < n - 1; i++) {
            int tens = digits[i];
            int maxUnits = -1;
            for (int j = i + 1; j < n; j++) {
                if (digits[j] > maxUnits) {
                    maxUnits = digits[j];
                }
            }
            int joltage = tens * 10 + maxUnits;
            if (joltage > maxJoltage) {
                maxJoltage = joltage;
            }
        }
        return maxJoltage;
    }

    @Override
    public Long getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
