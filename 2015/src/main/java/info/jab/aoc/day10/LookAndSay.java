package info.jab.aoc.day10;

public class LookAndSay {

    public String applyLookAndSay(String input, int iterations) {
        String sequence = input;
        for (int i = 0; i < iterations; i++) {
            sequence = generateNextSequence(sequence);
        }
        return sequence;
    }

    private String generateNextSequence(String sequence) {
        StringBuilder nextSequence = new StringBuilder();
        int count = 1;
        for (int i = 1; i < sequence.length(); i++) {
            if (sequence.charAt(i) == sequence.charAt(i - 1)) {
                count++;
            } else {
                nextSequence.append(count).append(sequence.charAt(i - 1));
                count = 1;
            }
        }
        
        // Append the last run
        nextSequence.append(count).append(sequence.charAt(sequence.length() - 1));
        return nextSequence.toString();
    }
}
