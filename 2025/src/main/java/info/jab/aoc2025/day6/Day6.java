package info.jab.aoc2025.day6;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.List;
import java.util.ArrayList;

public class Day6 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return solve(lines);
    }

    @Override
    public Long getPart2Result(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private Long solve(List<String> lines) {
        if (lines.isEmpty()) return 0L;

        int maxLength = lines.stream().mapToInt(String::length).max().orElse(0);
        List<String> paddedLines = new ArrayList<>();
        for (String line : lines) {
            StringBuilder sb = new StringBuilder(line);
            while (sb.length() < maxLength) {
                sb.append(' ');
            }
            paddedLines.add(sb.toString());
        }

        long totalSum = 0;
        int startCol = 0;

        for (int col = 0; col < maxLength; col++) {
            boolean isSeparator = true;
            for (String line : paddedLines) {
                if (line.charAt(col) != ' ') {
                    isSeparator = false;
                    break;
                }
            }

            if (isSeparator) {
                if (col > startCol) {
                    totalSum += processBlock(paddedLines, startCol, col);
                }
                startCol = col + 1;
            }
        }

        if (startCol < maxLength) {
            totalSum += processBlock(paddedLines, startCol, maxLength);
        }

        return totalSum;
    }

    private long processBlock(List<String> lines, int startCol, int endCol) {
        List<Long> numbers = new ArrayList<>();
        char operator = ' ';

        for (String line : lines) {
            String sub = line.substring(startCol, endCol).trim();
            if (sub.isEmpty()) continue;

            if (sub.equals("+") || sub.equals("*")) {
                operator = sub.charAt(0);
            } else {
                try {
                    numbers.add(Long.parseLong(sub));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
        }

        if (operator == '+') {
            return numbers.stream().mapToLong(Long::longValue).sum();
        } else if (operator == '*') {
            return numbers.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
        }
        
        return 0;
    }
}
