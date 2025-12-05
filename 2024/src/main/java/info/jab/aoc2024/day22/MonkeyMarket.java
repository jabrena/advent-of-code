package info.jab.aoc2024.day22;

import com.putoet.resources.ResourceLines;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.HashMap;

public class MonkeyMarket {


    // Function to generate the next secret number
    private long getNextSecret(long secret) {
        long res = secret;
        res = ((res << 6) ^ res) % 16777216;
        res = ((res >> 5) ^ res) % 16777216;
        res = ((res << 11 ) ^ res) % 16777216;
        return res;
    }

    private long[] getInputData(String fileName) {
        var list = ResourceLines.list(fileName);
        return list.stream().mapToLong(Long::parseLong).toArray();
    }

    public Long solvePartOne(String fileName) {
        long[] initialSecrets = getInputData(fileName);
        final int iterations = 2000;
        long result = 0;
        for (long num : initialSecrets) {
            long newSecret = num;
            for(int i = 0; i < iterations; i++) {
                newSecret = getNextSecret(newSecret);
            }
            result += newSecret;
        }
        return result;
    }

    record CacheKey(long a, long b, long c, long d) {
        public long[] getArray() {
            return new long[] { a, b, c, d };
        }
    }

    /**
     * Solves part two of the problem by processing a sequence of secrets and accumulating values
     * based on specific sequences of differences between consecutive secrets.
     *
     * <p>The method uses two primary data structures:</p>
     *
     * <ul>
     * <li><b>HashMap&lt;CacheKey, Long&gt; cache</b>: Stores the accumulated values for each unique sequence
     * of secret values. This allows for quick updates and retrieval of the accumulated value for a given sequence.</li>
     * <li><b>HashSet&lt;CacheKey&gt; processed</b>: Tracks which sequences have already been processed
     * in the current iteration, preventing redundant processing of the same sequence.</li>
     * </ul>
     *
     * <p>The flow of logic is as follows:</p>
     *
     * <ol>
     * <li>For each number in the initial secrets, a sequence of secrets is generated and stored in the array {@code seq}.</li>
     * <li>For each iteration, a new secret is generated using {@code getNextSecret(newSecret)}, and a difference value is calculated
     * as {@code seq[i] = ((a % 10) - (newSecret % 10))}.</li>
     * <li>If the sequence length is greater than or equal to 3, a {@code CacheKey} is formed using the last four values
     * of the sequence, and the program checks whether that sequence has already been processed for the current secret
     * using {@code processed.contains(key)}.</li>
     * <li>If the sequence hasn't been processed, it's added to the {@code processed} set, and the {@code cache} is updated
     * with the new secret's value (mod 10) by merging it with the current accumulated value.</li>
     * <li>Finally, after processing all secrets, the maximum accumulated value from the {@code cache} is returned.</li>
     * </ol>
     *
     * <p>The combination of the {@code HashMap} and {@code HashSet} enables the method to process large inputs efficiently
     * by avoiding redundant work and storing intermediate results in a cache for reuse.</p>
     */
    public Long solvePartTwo(String fileName) {
        long[] initialSecrets = getInputData(fileName);
        final int iterations = 2000;

        HashMap<CacheKey, Long> cache = new HashMap<>();
        for (long num : initialSecrets) {
            long newSecret = num;

            long[] seq = new long[iterations];
            HashSet<CacheKey> processed = new HashSet<>();

            for (int i = 0; i < iterations; i++) {
                var a = getNextSecret(newSecret);
                seq[i] = ((a % 10) - (newSecret % 10));
                newSecret = a;

                if (i >= 3) {
                    var key = new CacheKey(seq[i - 3], seq[i - 2], seq[i - 1], seq[i]);
                    if (!processed.contains(key)) {
                        processed.add(key);
                        cache.merge(key, newSecret % 10, Long::sum);
                    }
                }
            }
        }

        // Find the maximum accumulated value in the cache
        return cache.values().stream().mapToLong(Long::longValue).max().orElse(0L);
    }
}
