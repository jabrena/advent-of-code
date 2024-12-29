package info.jab.aoc.day1;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

class FrequencyDevice implements Solver<Integer> {

    private int parseChange(String change) {
        if (change == null || change.isEmpty())
            throw new IllegalArgumentException("Change cannot be null or empty");
            
        char sign = change.charAt(0);
        int value = Integer.parseInt(change.substring(1));
        
        return switch (sign) {
            case '+' -> value;
            case '-' -> -value;
            default -> throw new IllegalArgumentException("Invalid change format: " + change);
        };
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream()
                   .mapToInt(this::parseChange)
                   .sum();
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        throw new UnsupportedOperationException();
    }
}
