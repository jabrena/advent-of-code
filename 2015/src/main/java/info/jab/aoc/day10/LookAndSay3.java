package info.jab.aoc.day10;

public class LookAndSay3 {

    public String applyLookAndSay(String sequence, int iterations) {
        if (iterations == 0) {
            return sequence;
        }
        return applyLookAndSay(generateNextSequence(sequence), iterations - 1);
    }

    public String generateNextSequence(String sequence) {
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