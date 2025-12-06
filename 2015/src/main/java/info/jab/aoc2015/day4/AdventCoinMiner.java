package info.jab.aoc2015.day4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.IntStream;

public class AdventCoinMiner {

    public int findLowestNumber(String secretKey, boolean isPart1) {
        String prefix = isPart1 ? "00000" : "000000";
        
        // Use parallel processing for better performance on brute force search
        // Each thread creates its own MessageDigest instance for thread-safety
        return IntStream.iterate(1, n -> n + 1)
            .parallel()
            .filter(number -> hasValidHash(secretKey, number, prefix))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Not found solution"));
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
