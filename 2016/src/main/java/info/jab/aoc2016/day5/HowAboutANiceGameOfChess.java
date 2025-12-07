package info.jab.aoc2016.day5;

import com.putoet.resources.ResourceLines;
import com.putoet.security.MD5;
import info.jab.aoc.Solver;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Solver for Day 5: How About a Nice Game of Chess?
 * Generates passwords using MD5 hashes.
 * Optimized with parallel processing for improved performance.
 */
public final class HowAboutANiceGameOfChess implements Solver<String> {

    private static final int BATCH_SIZE = 10000;
    private static final int PASSWORD_LENGTH = 8;

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
        List<PasswordChar> found = new ArrayList<>();
        int startIndex = 0;
        
        while (found.size() < PASSWORD_LENGTH) {
            int endIndex = startIndex + BATCH_SIZE;
            
            List<PasswordChar> batchResults = IntStream.range(startIndex, endIndex)
                .parallel()
                .mapToObj(index -> {
                    String input = doorId + index;
                    String hash = MD5.hash(input).toLowerCase();
                    
                    if (hash.startsWith("00000")) {
                        return Optional.of(new PasswordChar(index, hash.charAt(5)));
                    }
                    return Optional.<PasswordChar>empty();
                })
                .flatMap(Optional::stream)
                .sorted(Comparator.comparingInt(PasswordChar::index))
                .limit(PASSWORD_LENGTH - found.size())
                .toList();
            
            found.addAll(batchResults);
            startIndex = endIndex;
        }
        
        return found.stream()
            .sorted(Comparator.comparingInt(PasswordChar::index))
            .limit(PASSWORD_LENGTH)
            .map(PasswordChar::character)
            .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
            .toString();
    }
    
    private String findPasswordWithPosition(final String doorId) {
        char[] password = new char[PASSWORD_LENGTH];
        boolean[] filled = new boolean[PASSWORD_LENGTH];
        AtomicInteger filledCount = new AtomicInteger(0);
        int startIndex = 0;
        
        while (filledCount.get() < PASSWORD_LENGTH) {
            int endIndex = startIndex + BATCH_SIZE;
            
            IntStream.range(startIndex, endIndex)
                .parallel()
                .forEach(index -> {
                    if (filledCount.get() >= PASSWORD_LENGTH) {
                        return;
                    }
                    
                    String input = doorId + index;
                    String hash = MD5.hash(input).toLowerCase();
                    
                    if (hash.startsWith("00000")) {
                        char positionChar = hash.charAt(5);
                        char valueChar = hash.charAt(6);
                        
                        // Check if position is valid (0-7)
                        if (positionChar >= '0' && positionChar <= '7') {
                            int position = positionChar - '0';
                            
                            // Use only the first result for each position (thread-safe check)
                            synchronized (password) {
                                if (!filled[position] && filledCount.get() < PASSWORD_LENGTH) {
                                    password[position] = valueChar;
                                    filled[position] = true;
                                    filledCount.incrementAndGet();
                                }
                            }
                        }
                    }
                });
            
            startIndex = endIndex;
        }
        
        return new String(password);
    }
    
    private record PasswordChar(int index, char character) {
    }
}

