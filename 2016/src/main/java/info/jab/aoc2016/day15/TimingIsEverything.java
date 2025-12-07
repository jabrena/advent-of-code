package info.jab.aoc2016.day15;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solver for Day 15: Timing Is Everything
 * Finds the first valid time when all discs are aligned.
 * Optimized using Chinese Remainder Theorem (CRT) for O(n) complexity.
 */
public final class TimingIsEverything implements Solver<Integer> {

    private static final Pattern DISC_PATTERN = Pattern.compile(
            "Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+)\\."
    );

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Disc> discs = parseDiscs(lines);
        return solveWithCRT(discs);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Disc> discs = parseDiscs(lines);
        // Add disc 7 for part 2
        discs.add(new Disc(7, 11, 0));
        return solveWithCRT(discs);
    }

    private List<Disc> parseDiscs(final List<String> lines) {
        List<Disc> discs = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = DISC_PATTERN.matcher(line);
            if (matcher.matches()) {
                int number = Integer.parseInt(matcher.group(1));
                int positions = Integer.parseInt(matcher.group(2));
                int initialPosition = Integer.parseInt(matcher.group(3));
                discs.add(new Disc(number, positions, initialPosition));
            }
        }
        return discs;
    }

    /**
     * Solves using Chinese Remainder Theorem (CRT).
     * For each disc i, we need: (initialPosition[i] + t + i) % positions[i] == 0
     * This means: t ≡ (-initialPosition[i] - i) mod positions[i]
     * 
     * Complexity: O(n) where n = number of discs
     */
    private int solveWithCRT(final List<Disc> discs) {
        int n = discs.size();
        long[] moduli = new long[n];
        long[] remainders = new long[n];
        
        // Build system of congruences
        for (int i = 0; i < n; i++) {
            Disc disc = discs.get(i);
            moduli[i] = disc.positions();
            // We need: (initialPosition + t + discNumber) % positions == 0
            // So: t ≡ (-initialPosition - discNumber) mod positions
            long remainder = (-disc.initialPosition() - disc.number()) % moduli[i];
            if (remainder < 0) {
                remainder += moduli[i];
            }
            remainders[i] = remainder;
        }
        
        return (int) chineseRemainderTheorem(moduli, remainders);
    }

    /**
     * Solves system of congruences using Chinese Remainder Theorem.
     * x ≡ remainders[i] (mod moduli[i]) for all i
     * 
     * @param moduli Array of pairwise coprime moduli
     * @param remainders Array of remainders
     * @return The smallest non-negative solution
     */
    private long chineseRemainderTheorem(final long[] moduli, final long[] remainders) {
        long product = 1;
        for (long m : moduli) {
            product *= m;
        }
        
        long result = 0;
        for (int i = 0; i < moduli.length; i++) {
            long mi = moduli[i];
            long ai = remainders[i];
            long Mi = product / mi;
            long yi = modularInverse(Mi, mi);
            result = (result + ai * Mi * yi) % product;
        }
        
        if (result < 0) {
            result += product;
        }
        return result;
    }

    /**
     * Computes modular inverse of a mod m using extended Euclidean algorithm.
     * Returns x such that (a * x) ≡ 1 (mod m)
     */
    private long modularInverse(final long a, final long m) {
        long[] gcdResult = extendedGcd(a, m);
        long x = gcdResult[1];
        if (x < 0) {
            x += m;
        }
        return x;
    }

    /**
     * Extended Euclidean algorithm.
     * Returns [gcd, x, y] such that gcd = a*x + b*y
     */
    private long[] extendedGcd(final long a, final long b) {
        if (b == 0) {
            return new long[]{a, 1, 0};
        }
        long[] result = extendedGcd(b, a % b);
        long gcd = result[0];
        long x1 = result[1];
        long y1 = result[2];
        return new long[]{gcd, y1, x1 - (a / b) * y1};
    }
}

