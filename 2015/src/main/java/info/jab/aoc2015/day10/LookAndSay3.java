package info.jab.aoc2015.day10;

import info.jab.aoc.Trampoline;

/**
 * Look-and-say sequence generator using trampoline pattern for safe deep recursion.
 * Follows functional programming principles by converting recursive calls to iteration.
 */
public class LookAndSay3 {

    /**
     * Applies the look-and-say transformation iteratively using trampoline pattern.
     * This avoids stack overflow for deep recursion while maintaining functional style.
     * 
     * @param sequence the initial sequence
     * @param iterations the number of iterations to apply
     * @return the final sequence after all iterations
     */
    public String applyLookAndSay(final String sequence, final int iterations) {
        return Trampoline.run(applyLookAndSayTrampoline(sequence, iterations));
    }

    /**
     * Creates a trampoline computation for applying look-and-say transformations.
     * Uses tail-recursive pattern converted to trampoline for stack safety.
     * 
     * @param sequence the current sequence
     * @param iterations remaining iterations
     * @return trampoline computation that will eventually return the final sequence
     */
    private Trampoline<String> applyLookAndSayTrampoline(final String sequence, final int iterations) {
        if (iterations == 0) {
            return new Trampoline.Done<>(sequence);
        }
        return new Trampoline.More<>(() -> 
            applyLookAndSayTrampoline(generateNextSequence(sequence), iterations - 1)
        );
    }

    /**
     * Generates the next sequence in the look-and-say pattern.
     * Pure function: no side effects, returns new string based on input.
     * 
     * @param sequence the current sequence
     * @return the next sequence in the look-and-say pattern
     */
    public String generateNextSequence(final String sequence) {
        if (sequence.isEmpty()) {
            return sequence;
        }
        
        final StringBuilder nextSequence = new StringBuilder();
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