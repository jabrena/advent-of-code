package info.jab.aoc2015.day10;

public class LookAndSay {

    public String applyLookAndSay(String input, int iterations) {
        String sequence = input;
        for (int i = 0; i < iterations; i++) {
            sequence = generateNextSequence(sequence);
        }
        return sequence;
    }

    private String generateNextSequence(String sequence) {
        if (sequence.isEmpty()) {
            return sequence;
        }
        
        StringBuilder nextSequence = new StringBuilder();
        char currentChar = sequence.charAt(0);
        int count = 1;
        
        for (int i = 1; i < sequence.length(); i++) {
            char nextChar = sequence.charAt(i);
            if (nextChar == currentChar) {
                count++;
            } else {
                appendRun(nextSequence, count, currentChar);
                currentChar = nextChar;
                count = 1;
            }
        }
        
        // Append the final run
        appendRun(nextSequence, count, currentChar);
        return nextSequence.toString();
    }

    private void appendRun(StringBuilder builder, int count, char character) {
        builder.append(count).append(character);
    }
}
