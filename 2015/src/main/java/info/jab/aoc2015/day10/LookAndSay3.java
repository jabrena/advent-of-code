package info.jab.aoc2015.day10;

import info.jab.aoc.Trampoline;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
     * Generates the next sequence in the look-and-say pattern using Stream API.
     * Pure function: no side effects, returns new string based on input.
     * Groups consecutive identical characters and formats as "count" + "character".
     * 
     * @param sequence the current sequence
     * @return the next sequence in the look-and-say pattern
     */
    public String generateNextSequence(final String sequence) {
        if (sequence.isEmpty()) {
            return sequence;
        }
        
        // Group consecutive characters and convert to "count" + "character" format
        return IntStream.range(0, sequence.length())
                .collect(
                    ArrayList<Run>::new,
                    (runs, index) -> {
                        final char currentChar = sequence.charAt(index);
                        if (runs.isEmpty() || runs.getLast().character() != currentChar) {
                            runs.add(new Run(currentChar, 1));
                        } else {
                            final Run lastRun = runs.getLast();
                            runs.set(runs.size() - 1, new Run(lastRun.character(), lastRun.count() + 1));
                        }
                    },
                    (runs1, runs2) -> {
                        if (!runs1.isEmpty() && !runs2.isEmpty() 
                                && runs1.getLast().character() == runs2.getFirst().character()) {
                            final Run lastRun1 = runs1.getLast();
                            final Run firstRun2 = runs2.getFirst();
                            runs1.set(runs1.size() - 1, 
                                new Run(lastRun1.character(), lastRun1.count() + firstRun2.count()));
                            runs1.addAll(runs2.subList(1, runs2.size()));
                        } else {
                            runs1.addAll(runs2);
                        }
                    }
                )
                .stream()
                .map(run -> run.count() + String.valueOf(run.character()))
                .collect(java.util.stream.Collectors.joining());
    }
    
    /**
     * Represents a run of consecutive identical characters.
     * Immutable record following functional programming principles.
     */
    private record Run(char character, int count) {}
}