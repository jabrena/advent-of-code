package info.jab.aoc2016.day7;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Solver for Day 7: Internet Protocol Version 7
 * Validates IP addresses for TLS and SSL support.
 */
public final class InternetProtocolVersion7 implements Solver<Integer> {

    private static final Pattern HYPERNET_PATTERN = Pattern.compile("\\[([^\\]]+)\\]");
    
    /**
     * Check if a string contains an ABBA pattern.
     * An ABBA is a four-character sequence like 'abba' or 'xyyx' where
     * the first and fourth characters are the same, and the second and third
     * characters are the same, but different from the first/fourth.
     */
    private boolean containsABBA(final String text) {
        for (int i = 0; i <= text.length() - 4; i++) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            char c = text.charAt(i + 2);
            char d = text.charAt(i + 3);
            
            // Check if it's an ABBA pattern: a != b and a == d and b == c
            if (a != b && a == d && b == c) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Split an IP address into supernet sequences (outside brackets) and 
     * hypernet sequences (inside brackets).
     */
    private IPParts parseIP(final String ip) {
        List<String> supernetSequences = new ArrayList<>();
        List<String> hypernetSequences = new ArrayList<>();
        
        var matcher = HYPERNET_PATTERN.matcher(ip);
        int lastEnd = 0;
        
        while (matcher.find()) {
            // Add supernet sequence before the bracket
            if (matcher.start() > lastEnd) {
                supernetSequences.add(ip.substring(lastEnd, matcher.start()));
            }
            // Add hypernet sequence (inside brackets)
            hypernetSequences.add(matcher.group(1));
            lastEnd = matcher.end();
        }
        
        // Add remaining supernet sequence after the last bracket
        if (lastEnd < ip.length()) {
            supernetSequences.add(ip.substring(lastEnd));
        }
        
        return new IPParts(supernetSequences, hypernetSequences);
    }
    
    /**
     * Check if an IP address supports TLS.
     * It supports TLS if:
     * 1. At least one supernet sequence contains an ABBA pattern
     * 2. No hypernet sequence contains an ABBA pattern
     */
    public boolean supportsTLS(final String ip) {
        IPParts parts = parseIP(ip);
        
        // Check if any hypernet sequence contains ABBA (if so, TLS is not supported)
        for (String hypernet : parts.hypernetSequences()) {
            if (containsABBA(hypernet)) {
                return false;
            }
        }
        
        // Check if any supernet sequence contains ABBA
        for (String supernet : parts.supernetSequences()) {
            if (containsABBA(supernet)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Find all ABA patterns in a string.
     * An ABA is a three-character sequence where the first and third characters
     * are the same, but different from the middle character.
     */
    private List<String> findABAPatterns(final String text) {
        List<String> abaPatterns = new ArrayList<>();
        for (int i = 0; i <= text.length() - 3; i++) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            char c = text.charAt(i + 2);
            
            // Check if it's an ABA pattern: a != b and a == c
            if (a != b && a == c) {
                abaPatterns.add(text.substring(i, i + 3));
            }
        }
        return abaPatterns;
    }
    
    /**
     * Convert an ABA pattern to its corresponding BAB pattern.
     * For example: "aba" -> "bab", "xyx" -> "yxy"
     */
    private String abaToBAB(final String aba) {
        if (aba.length() != 3) {
            throw new IllegalArgumentException("ABA pattern must be exactly 3 characters");
        }
        char a = aba.charAt(0);
        char b = aba.charAt(1);
        return "" + b + a + b;
    }
    
    /**
     * Check if an IP address supports SSL.
     * It supports SSL if it has an ABA pattern in supernet sequences
     * and a corresponding BAB pattern in hypernet sequences.
     */
    public boolean supportsSSL(final String ip) {
        IPParts parts = parseIP(ip);
        
        // Find all ABA patterns in supernet sequences
        List<String> abaPatterns = new ArrayList<>();
        for (String supernet : parts.supernetSequences()) {
            abaPatterns.addAll(findABAPatterns(supernet));
        }
        
        // Find all ABA patterns in hypernet sequences (these will be treated as potential BAB patterns)
        List<String> hypernetABAs = new ArrayList<>();
        for (String hypernet : parts.hypernetSequences()) {
            hypernetABAs.addAll(findABAPatterns(hypernet));
        }
        
        // Check if any ABA pattern from supernet has a corresponding BAB in hypernet
        for (String aba : abaPatterns) {
            String correspondingBAB = abaToBAB(aba);
            if (hypernetABAs.contains(correspondingBAB)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return (int) lines.stream()
                .mapToInt(ip -> supportsTLS(ip) ? 1 : 0)
                .sum();
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return (int) lines.stream()
                .mapToInt(ip -> supportsSSL(ip) ? 1 : 0)
                .sum();
    }
    
    private record IPParts(List<String> supernetSequences, List<String> hypernetSequences) {}
}

