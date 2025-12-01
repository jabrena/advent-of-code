package info.jab.aoc2015.day5;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

import java.util.Arrays;
import java.util.stream.IntStream;

public class StringValidator implements Solver<Integer> {

    private static final String VOWELS = "aeiou";
    private static final String[] FORBIDDEN_SUBSTRINGS = {"ab", "cd", "pq", "xy"};

    private boolean hasThreeVowels(String input) {
        return input.chars()
                   .mapToObj(c -> (char) c)
                   .filter(c -> VOWELS.indexOf(c) != -1)
                   .count() >= 3;
    }

    private boolean hasDoubleLetter(String input) {
        return IntStream.range(0, input.length() - 1)
            .anyMatch(i -> input.charAt(i) == input.charAt(i + 1));
    }

    private boolean containsForbiddenSubstrings(String input) {
        return Arrays.stream(FORBIDDEN_SUBSTRINGS).anyMatch(input::contains);
    }

    private boolean hasRepeatingPair(String input) {
        return IntStream.range(0, input.length() - 1)
                    .anyMatch(i -> {
                        String pair = input.substring(i, i + 2);
                        return input.indexOf(pair, i + 2) != -1;
                    });
    }

    private boolean hasRepeatingLetterWithOneBetween(String input) {
        return IntStream.range(0, input.length() - 2)
                    .anyMatch(i -> input.charAt(i) == input.charAt(i + 2));
    }

    public boolean isNiceString(String input) {
        return hasThreeVowels(input) && 
               hasDoubleLetter(input) && 
               !containsForbiddenSubstrings(input);
    }

    public boolean isNiceStringPartTwo(String input) {
        return hasRepeatingPair(input) && 
               hasRepeatingLetterWithOneBetween(input);
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        return (int) lines.stream()
            .filter(this::isNiceString)
            .count();
    }

    @Override 
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        return (int) lines.stream()
            .filter(this::isNiceStringPartTwo)
            .count();
    }
} 