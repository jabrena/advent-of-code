package info.jab.aoc2015.day4;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.IntStream;

public class AdventCoinMiner implements Solver<Integer> {

    /**
     * Maximum number of iterations allowed to prevent DoS attacks.
     * This limit prevents infinite loops when searching for valid hashes.
     */
    private static final int MAX_ITERATIONS = 10_000_000;

    @Override
    public Integer solvePartOne(final String fileName) {
        String secretKey = ResourceLines.line(fileName);
        return findLowestNumber(secretKey, true);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        String secretKey = ResourceLines.line(fileName);
        return findLowestNumber(secretKey, false);
    }

    private int findLowestNumber(String secretKey, boolean isPart1) {
        String prefix = isPart1 ? "00000" : "000000";
        
        // Use parallel processing for better performance on brute force search
        // Each thread creates its own MessageDigest instance for thread-safety
        return IntStream.iterate(1, n -> n + 1)
            .limit(MAX_ITERATIONS)
            .parallel()
            .filter(number -> hasValidHash(secretKey, number, prefix))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(
                    "Not found solution within " + MAX_ITERATIONS + " iterations"));
    }

    private boolean hasValidHash(String secretKey, int number, String prefix) {
        try {
            // Suppressed: MD5 is required for Advent of Code challenge
            @SuppressWarnings("java:S4790")
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            String input = secretKey + number;
            md.update(input.getBytes());
            String hash = HexFormat.of().formatHex(md.digest());
            return hash.startsWith(prefix);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available: " + e.getMessage(), e);
        }
    }
}
