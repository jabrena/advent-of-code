package info.jab.aoc2016.day5;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Solver for Day 5: How About a Nice Game of Chess?
 * Generates passwords using MD5 hashes.
 * Optimized with parallel processing and zero-allocation checks.
 */
public final class HowAboutANiceGameOfChess implements Solver<String> {

    private static final int BATCH_SIZE = 10000;
    private static final int PASSWORD_LENGTH = 8;
    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    @Override
    public String solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String doorId = lines.get(0);
        return findPassword(doorId);
    }

    @Override
    public String solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        String doorId = lines.get(0);
        return findPasswordWithPosition(doorId);
    }
    
    private String findPassword(final String doorId) {
        StringBuilder result = new StringBuilder();
        int startIndex = 0;
        
        try {
            while (result.length() < PASSWORD_LENGTH) {
                int endIndex = startIndex + BATCH_SIZE;
                
                // Collect all matches in this batch
                var batchMatches = IntStream.range(startIndex, endIndex)
                    .parallel()
                    .boxed()
                    .flatMap(i -> {
                        MD5Worker worker = getWorker();
                        byte[] digest = worker.hash(doorId + i);
                        if (startsWithFiveZeros(digest)) {
                            // 6th char is the low nibble of 3rd byte
                            char c = HEX_ARRAY[digest[2] & 0x0F];
                            return Stream.of(new Match(i, c));
                        }
                        return Stream.empty();
                    })
                    .sorted((a, b) -> Integer.compare(a.index, b.index))
                    .toList();
                
                for (Match m : batchMatches) {
                    if (result.length() < PASSWORD_LENGTH) {
                        result.append(m.character);
                    }
                }
                
                startIndex = endIndex;
            }
        } finally {
            WORKER.remove();
        }
        
        return result.toString();
    }
    
    private record Match(int index, char character) {}
    
    private String findPasswordWithPosition(final String doorId) {
        char[] password = new char[PASSWORD_LENGTH];
        boolean[] filled = new boolean[PASSWORD_LENGTH];
        AtomicInteger filledCount = new AtomicInteger(0);
        
        int startIndex = 0;
        
        try {
            while (filledCount.get() < PASSWORD_LENGTH) {
                int endIndex = startIndex + BATCH_SIZE;
                
                var batchMatches = IntStream.range(startIndex, endIndex)
                    .parallel()
                    .boxed()
                    .flatMap(i -> {
                        MD5Worker worker = getWorker();
                        byte[] digest = worker.hash(doorId + i);
                        if (startsWithFiveZeros(digest)) {
                            // Position: 6th char (low nibble of byte 2)
                            int posIndex = digest[2] & 0x0F;
                            // Value: 7th char (high nibble of byte 3)
                            int valIndex = (digest[3] & 0xF0) >>> 4;
                            char val = HEX_ARRAY[valIndex];
                            return Stream.of(new PosMatch(i, posIndex, val));
                        }
                        return Stream.empty();
                    })
                    .sorted((a, b) -> Integer.compare(a.index, b.index))
                    .toList();
                
                for (PosMatch m : batchMatches) {
                    if (m.pos < PASSWORD_LENGTH && !filled[m.pos]) {
                        password[m.pos] = m.val;
                        filled[m.pos] = true;
                        filledCount.incrementAndGet();
                        if (filledCount.get() == PASSWORD_LENGTH) break;
                    }
                }
                
                startIndex = endIndex;
            }
        } finally {
            WORKER.remove();
        }
        
        return new String(password);
    }
    
    private record PosMatch(int index, int pos, char val) {}
    
    private boolean startsWithFiveZeros(byte[] digest) {
        return digest[0] == 0 && digest[1] == 0 && (digest[2] & 0xF0) == 0;
    }
    
    private static final ThreadLocal<MD5Worker> WORKER = ThreadLocal.withInitial(MD5Worker::new);
    
    private static MD5Worker getWorker() {
        return WORKER.get();
    }

    private static class MD5Worker {
        private final MessageDigest md;

        MD5Worker() {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        
        byte[] hash(String input) {
            md.reset();
            md.update(input.getBytes());
            return md.digest();
        }
    }
}

