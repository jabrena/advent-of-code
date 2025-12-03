package info.jab.aoc2025.day3;

import info.jab.aoc.Day;
import info.jab.aoc.Utils;
import java.util.List;

public class Day3 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        return solve(fileName, 2);
    }

    @Override
    public Long getPart2Result(String fileName) {
        return solve(fileName, 12);
    }

    private Long solve(String fileName, int length) {
        List<String> lines = Utils.readFileToList(fileName);
        long totalJoltage = 0;
        for (String line : lines) {
            if (line.length() >= length) {
                totalJoltage += getMaxJoltage(line, length);
            }
        }
        return totalJoltage;
    }

    private long getMaxJoltage(String line, int length) {
        int n = line.length();
        int[] digits = new int[n];
        for (int i = 0; i < n; i++) {
            digits[i] = line.charAt(i) - '0';
        }

        long result = 0;
        int currentPos = 0;
        for (int rem = length; rem > 0; rem--) {
            int maxVal = -1;
            int maxIdx = -1;
            for (int i = currentPos; i <= n - rem; i++) {
                if (digits[i] > maxVal) {
                    maxVal = digits[i];
                    maxIdx = i;
                }
            }
            if (maxIdx != -1) {
                result = result * 10 + maxVal;
                currentPos = maxIdx + 1;
            }
        }
        return result;
    }
}
