package info.jab.aoc.day2;

import info.jab.aoc.Solver;

import com.putoet.resources.ResourceLines;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day2 implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> boxIds = ResourceLines.list(fileName);
        
        long countWithExactlyTwo = boxIds.stream()
                .filter(this::hasExactlyTwoOfAnyLetter)
                .count();
        
        long countWithExactlyThree = boxIds.stream()
                .filter(this::hasExactlyThreeOfAnyLetter)
                .count();
        
        return (int) (countWithExactlyTwo * countWithExactlyThree);
    }

    private boolean hasExactlyTwoOfAnyLetter(String boxId) {
        Map<Character, Long> charCounts = boxId.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        return charCounts.values().stream()
                .anyMatch(count -> count == 2L);
    }

    private boolean hasExactlyThreeOfAnyLetter(String boxId) {
        Map<Character, Long> charCounts = boxId.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        return charCounts.values().stream()
                .anyMatch(count -> count == 3L);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
