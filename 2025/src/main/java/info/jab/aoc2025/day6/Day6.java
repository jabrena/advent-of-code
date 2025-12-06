package info.jab.aoc2025.day6;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.List;
import java.util.ArrayList;
import java.util.function.BiFunction;

public class Day6 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return solve(lines, this::processBlockPart1);
    }

    @Override
    public Long getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return solve(lines, this::processBlockPart2);
    }

    private Long solve(List<String> lines, BiFunction<List<String>, Integer[], Long> blockProcessor) {
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
                    totalSum += blockProcessor.apply(paddedLines, new Integer[]{startCol, col});
                }
                startCol = col + 1;
            }
        }

        if (startCol < maxLength) {
            totalSum += blockProcessor.apply(paddedLines, new Integer[]{startCol, maxLength});
        }

        return totalSum;
    }

    private Long processBlockPart1(List<String> lines, Integer[] range) {
        int startCol = range[0];
        int endCol = range[1];
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

        return calculate(numbers, operator);
    }

    private Long processBlockPart2(List<String> lines, Integer[] range) {
        int startCol = range[0];
        int endCol = range[1];
        List<Long> numbers = new ArrayList<>();
        char operator = ' ';

        // Find operator
        for (String line : lines) {
             String sub = line.substring(startCol, endCol).trim();
             if (sub.contains("+")) { operator = '+'; break; }
             if (sub.contains("*")) { operator = '*'; break; }
        }

        // Iterate columns right to left
        for (int col = endCol - 1; col >= startCol; col--) {
            StringBuilder numStr = new StringBuilder();
            // Iterate rows, exclude last row?
            // The problem says "symbol at the bottom of the problem is still the operator".
            // So we should check if the character is a digit.
            
            for (String line : lines) {
                char c = line.charAt(col);
                if (Character.isDigit(c)) {
                    numStr.append(c);
                }
            }
            
            if (numStr.length() > 0) {
                numbers.add(Long.parseLong(numStr.toString()));
            }
        }

        return calculate(numbers, operator);
    }

    private long calculate(List<Long> numbers, char operator) {
        if (numbers.isEmpty()) return 0;
        
        if (operator == '+') {
            return numbers.stream().mapToLong(Long::longValue).sum();
        } else if (operator == '*') {
            return numbers.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
        }
        return 0;
    }
}
