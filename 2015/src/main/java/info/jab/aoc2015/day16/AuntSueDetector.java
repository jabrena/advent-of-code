package info.jab.aoc2015.day16;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.*;
import java.util.regex.Pattern;

public class AuntSueDetector implements Solver<Integer> {

    /**
     * Maximum input length to prevent ReDoS attacks.
     * This limit prevents polynomial runtime due to regex backtracking.
     */
    private static final int MAX_INPUT_LENGTH = 10_000;

    /**
     * Maximum number of regex matches to prevent DoS attacks.
     * This limit prevents excessive iterations in regex find() loops.
     */
    private static final int MAX_MATCHES = 100;

    // The MFCSAM analysis results
    private static final Map<String, Integer> MFCSAM_ANALYSIS = Map.of(
        "children", 3,
        "cats", 7,
        "samoyeds", 2,
        "pomeranians", 3,
        "akitas", 0,
        "vizslas", 0,
        "goldfish", 5,
        "trees", 3,
        "cars", 2,
        "perfumes", 1
    );

    // Use atomic group wrapped in capturing group to prevent backtracking: ((?>.*)) instead of (.*)
    // Atomic groups prevent backtracking, making the regex safe from ReDoS attacks
    // The pattern matches: "Sue <number>: <rest of line>"
    // Outer parentheses create capturing group 2, inner atomic group prevents backtracking
    private static final Pattern SUE_PATTERN = Pattern.compile("Sue (\\d+): ((?>.*))");
    // Use atomic groups for compound pattern to prevent backtracking
    private static final Pattern COMPOUND_PATTERN = Pattern.compile("((?>\\w+)): ((?>\\d+))");

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);

        for (String line : lines) {
            if (line.length() > MAX_INPUT_LENGTH) {
                throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
            }

            var sueMatcher = SUE_PATTERN.matcher(line);
            if (sueMatcher.matches()) {
                int sueNumber = Integer.parseInt(sueMatcher.group(1));
                String compounds = sueMatcher.group(2);

                if (matchesAnalysis(compounds, false)) {
                    return sueNumber;
                }
            }
        }

        return -1; // Not found
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);

        for (String line : lines) {
            if (line.length() > MAX_INPUT_LENGTH) {
                throw new IllegalArgumentException("Input line exceeds maximum length of " + MAX_INPUT_LENGTH);
            }

            var sueMatcher = SUE_PATTERN.matcher(line);
            if (sueMatcher.matches()) {
                int sueNumber = Integer.parseInt(sueMatcher.group(1));
                String compounds = sueMatcher.group(2);

                if (matchesAnalysis(compounds, true)) {
                    return sueNumber;
                }
            }
        }

        return -1; // Not found
    }

    private boolean matchesAnalysis(String compounds, boolean isPart2) {
        if (compounds.length() > MAX_INPUT_LENGTH) {
            throw new IllegalArgumentException("Compounds string exceeds maximum length of " + MAX_INPUT_LENGTH);
        }

        var compoundMatcher = COMPOUND_PATTERN.matcher(compounds);
        int matchCount = 0;

        while (compoundMatcher.find()) {
            if (matchCount >= MAX_MATCHES) {
                throw new IllegalStateException("Exceeded maximum number of matches: " + MAX_MATCHES);
            }
            matchCount++;

            String compound = compoundMatcher.group(1);
            int value = Integer.parseInt(compoundMatcher.group(2));

            if (!MFCSAM_ANALYSIS.containsKey(compound)) {
                continue; // Skip unknown compounds
            }

            int expectedValue = MFCSAM_ANALYSIS.get(compound);

            if (isPart2) {
                // Part 2: cats and trees should be greater, pomeranians and goldfish should be fewer
                if ("cats".equals(compound) || "trees".equals(compound)) {
                    if (value <= expectedValue) {
                        return false;
                    }
                } else if ("pomeranians".equals(compound) || "goldfish".equals(compound)) {
                    if (value >= expectedValue) {
                        return false;
                    }
                } else {
                    if (value != expectedValue) {
                        return false;
                    }
                }
            } else {
                // Part 1: exact match
                if (value != expectedValue) {
                    return false;
                }
            }
        }

        return true;
    }
}
