package info.jab.aoc2015.day10;

import java.util.stream.IntStream;


public class LookAndSay2 {

    public String applyLookAndSay(String input, int iterations) {
        return IntStream.range(0, iterations).boxed()
                .reduce(input, (seq, step) -> generateNextSequence(seq), (s1, s2) -> s2);
    }

    private String generateNextSequence(String sequence) {
        StringBuilder result = new StringBuilder();

        IntStream.range(0, sequence.length())
                .filter(i -> i == sequence.length() - 1 || sequence.charAt(i) != sequence.charAt(i + 1))
                .forEach(i -> {
                    int count = i == 0 ? 1 : countConsecutive(sequence, i);
                    result.append(count).append(sequence.charAt(i));
                });

        return result.toString();
    }

    private int countConsecutive(String sequence, int endIndex) {
        char currentChar = sequence.charAt(endIndex);
        int count = 0;
        for (int i = endIndex; i >= 0 && sequence.charAt(i) == currentChar; i--) {
            count++;
        }
        return count;
    }
}