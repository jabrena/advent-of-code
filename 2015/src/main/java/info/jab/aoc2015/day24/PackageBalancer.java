package info.jab.aoc2015.day24;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Solver for package balancing problems.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Stream API for declarative transformations
 * - Immutable data structures
 */
public final class PackageBalancer implements Solver<Long> {

    @Override
    public Long solvePartOne(final String fileName) {
        List<Integer> packages = ResourceLines.list(fileName)
                .stream()
                .map(Integer::parseInt)
                .toList();

        int totalWeight = packages.stream().mapToInt(Integer::intValue).sum();
        int targetWeight = totalWeight / 3;

        return findMinimumQuantumEntanglement(packages, targetWeight);
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        List<Integer> packages = ResourceLines.list(fileName)
                .stream()
                .map(Integer::parseInt)
                .toList();

        int totalWeight = packages.stream().mapToInt(Integer::intValue).sum();
        int targetWeight = totalWeight / 4; // Divide into 4 groups instead of 3

        return findMinimumQuantumEntanglement(packages, targetWeight);
    }

    /**
     * Pure function: finds minimum quantum entanglement using functional approach.
     */
    private long findMinimumQuantumEntanglement(final List<Integer> packages, final int targetWeight) {
        // Sort packages in descending order to find smaller combinations faster
        // Larger packages first means we'll find minimum-size combinations earlier
        final List<Integer> sortedPackages = packages.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        // Progressive search: find the minimum size first using stream
        final int minSize = IntStream.rangeClosed(1, packages.size())
                .filter(maxSize -> findBestCombination(
                        sortedPackages,
                        targetWeight,
                        0,
                        List.of(),
                        maxSize,
                        Long.MAX_VALUE
                ) < Long.MAX_VALUE)
                .findFirst()
                .orElse(Integer.MAX_VALUE);

        if (minSize == Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        }

        // Now find the minimum quantum entanglement among all combinations of minimum size
        return findBestCombination(
                sortedPackages,
                targetWeight,
                0,
                List.of(),
                minSize,
                Long.MAX_VALUE
        );
    }

    /**
     * Pure recursive function: finds best combination using immutable lists.
     */
    private long findBestCombination(
            final List<Integer> packages,
            final int remainingWeight,
            final int startIndex,
            final List<Integer> currentCombination,
            final int maxSize,
            final long currentBest) {

        // Early termination: if current combination is already too large
        if (currentCombination.size() > maxSize) {
            return Long.MAX_VALUE;
        }

        // Early termination: if quantum entanglement already exceeds current best
        if (!currentCombination.isEmpty() && currentBest < Long.MAX_VALUE) {
            final long currentQE = calculateQuantumEntanglement(currentCombination);
            if (currentQE >= currentBest) {
                return Long.MAX_VALUE; // Prune this branch
            }
        }

        if (remainingWeight == 0) {
            // Found valid combination - check if it's the right size
            if (currentCombination.size() <= maxSize) {
                return calculateQuantumEntanglement(currentCombination);
            }
            return Long.MAX_VALUE;
        }

        if (remainingWeight < 0 || startIndex >= packages.size()) {
            return Long.MAX_VALUE;
        }

        // Early pruning: if we can't possibly reach target with remaining packages
        if (currentCombination.size() == maxSize && remainingWeight > 0) {
            return Long.MAX_VALUE;
        }

        final int packageWeight = packages.get(startIndex);
        final long newBest = Math.min(currentBest, Long.MAX_VALUE);

        // Try including current package
        final long qeWith = (remainingWeight >= packageWeight && currentCombination.size() < maxSize)
                ? findBestCombination(
                        packages,
                        remainingWeight - packageWeight,
                        startIndex + 1,
                        append(currentCombination, packageWeight),
                        maxSize,
                        newBest
                )
                : Long.MAX_VALUE;

        // Try excluding current package
        final long qeWithout = findBestCombination(
                packages,
                remainingWeight,
                startIndex + 1,
                currentCombination,
                maxSize,
                Math.min(newBest, qeWith)
        );

        return Math.min(qeWith, qeWithout);
    }
    
    /**
     * Pure function: creates new list with appended element (immutable).
     */
    private List<Integer> append(final List<Integer> list, final int value) {
        return java.util.stream.Stream.concat(list.stream(), java.util.stream.Stream.of(value))
                .toList();
    }

    /**
     * Pure function: calculates quantum entanglement using stream reduce.
     */
    private long calculateQuantumEntanglement(final List<Integer> packages) {
        return packages.stream()
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
    }
}
