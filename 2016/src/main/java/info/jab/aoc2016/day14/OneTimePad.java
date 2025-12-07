package info.jab.aoc2016.day14;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Solver for Day 14: One-Time Pad
 * Finds keys in a one-time pad using MD5 hashes.
 */
public final class OneTimePad implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return solve(salt, 64, false);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String salt = lines.get(0).trim();
        return solve(salt, 64, true);
    }

    private int solve(String salt, int n, boolean stretched) {
        // Window size needs to be 1001 (current + 1000 future)
        // We'll keep a buffer larger than that to batch computations
        List<String> window = new ArrayList<>(2000);
        int index = 0;
        int keysFound = 0;
        
        // Initial fill
        fillWindow(window, salt, index, 2000, stretched);
        
        while (keysFound < n) {
            // Ensure window has enough elements for lookahead (1000 items)
            // If we drop below 1001, we fetch another batch
            if (window.size() <= 1001) {
                int currentEndIndex = index + window.size();
                fillWindow(window, salt, currentEndIndex, 1000, stretched);
            }
            
            String currentHash = window.remove(0);
            Character triplet = findTriplet(currentHash);
            
            if (triplet != null) {
                // Check next 1000 hashes
                if (hasQuintuplet(window, triplet)) {
                    keysFound++;
                    if (keysFound == n) {
                        return index;
                    }
                }
            }
            index++;
        }
        
        return -1;
    }
    
    private void fillWindow(List<String> window, String salt, int startIndex, int count, boolean stretched) {
        List<String> newHashes = IntStream.range(startIndex, startIndex + count)
            .parallel()
            .mapToObj(i -> {
                MD5Worker w = getWorker();
                return stretched ? w.stretchedHash(salt + i) : w.hash(salt + i);
            })
            .toList();
        window.addAll(newHashes);
    }

    private Character findTriplet(final String hash) {
        int len = hash.length();
        for (int i = 0; i < len - 2; i++) {
            char c = hash.charAt(i);
            if (hash.charAt(i + 1) == c && hash.charAt(i + 2) == c) {
                return c;
            }
        }
        return null;
    }

    private boolean hasQuintuplet(final List<String> window, final char character) {
        String quintuplet = String.valueOf(character).repeat(5);
        // Check next 1000 items
        for (int i = 0; i < 1000 && i < window.size(); i++) {
            if (window.get(i).contains(quintuplet)) {
                return true;
            }
        }
        return false;
    }

    // ThreadLocal to reuse MessageDigest
    private static final ThreadLocal<MD5Worker> WORKER = ThreadLocal.withInitial(MD5Worker::new);
    
    private static MD5Worker getWorker() {
        return WORKER.get();
    }

    private static class MD5Worker {
        private final MessageDigest md;
        private final char[] hexArray = "0123456789abcdef".toCharArray();

        MD5Worker() {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        
        String hash(String input) {
            md.reset();
            md.update(input.getBytes());
            return bytesToHex(md.digest());
        }
        
        String stretchedHash(String input) {
            String h = hash(input);
            for (int i = 0; i < 2016; i++) {
                h = hash(h);
            }
            return h;
        }

        private String bytesToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
    }
}

