package info.jab.aoc2015.day24;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PackageBalancerSolver implements Solver<Long> {

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

    private long findMinimumQuantumEntanglement(List<Integer> packages, int targetWeight) {
        // Sort packages in descending order to find smaller combinations faster
        // Larger packages first means we'll find minimum-size combinations earlier
        List<Integer> sortedPackages = new ArrayList<>(packages);
        sortedPackages.sort(Collections.reverseOrder());

        // Progressive search: find the minimum size first
        int minSize = findMinimumSize(sortedPackages, targetWeight);
        if (minSize == Integer.MAX_VALUE) {
            return Long.MAX_VALUE;
        }

        // Now find the minimum quantum entanglement among all combinations of minimum size
        return findBestCombination(
            sortedPackages,
            targetWeight,
            0,
            new ArrayList<>(),
            minSize,
            Long.MAX_VALUE
        );
    }

    private int findMinimumSize(List<Integer> packages, int targetWeight) {
        // Search for combinations of increasing size until we find at least one valid combination
        for (int maxSize = 1; maxSize <= packages.size(); maxSize++) {
            long result = findBestCombination(
                packages,
                targetWeight,
                0,
                new ArrayList<>(),
                maxSize,
                Long.MAX_VALUE
            );

            if (result < Long.MAX_VALUE) {
                return maxSize; // Found minimum size
            }
        }
        return Integer.MAX_VALUE;
    }

    private long findBestCombination(
        List<Integer> packages,
        int remainingWeight,
        int startIndex,
        List<Integer> currentCombination,
        int maxSize,
        long currentBest) {

        // Early termination: if current combination is already too large
        if (currentCombination.size() > maxSize) {
            return Long.MAX_VALUE;
        }

        // Early termination: if quantum entanglement already exceeds current best
        if (currentCombination.size() > 0 && currentBest < Long.MAX_VALUE) {
            long currentQE = calculateQuantumEntanglement(currentCombination);
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

        long bestQE = Long.MAX_VALUE;

        // Try including current package
        int packageWeight = packages.get(startIndex);
        if (remainingWeight >= packageWeight && currentCombination.size() < maxSize) {
            currentCombination.add(packageWeight);
            long qeWith = findBestCombination(
                packages,
                remainingWeight - packageWeight,
                startIndex + 1,
                currentCombination,
                maxSize,
                Math.min(currentBest, bestQE)
            );
            bestQE = Math.min(bestQE, qeWith);
            currentCombination.remove(currentCombination.size() - 1);
        }

        // Try excluding current package
        long qeWithout = findBestCombination(
            packages,
            remainingWeight,
            startIndex + 1,
            currentCombination,
            maxSize,
            Math.min(currentBest, bestQE)
        );
        bestQE = Math.min(bestQE, qeWithout);

        return bestQE;
    }

    private long calculateQuantumEntanglement(List<Integer> packages) {
        return packages.stream()
                .mapToLong(Integer::longValue)
                .reduce(1L, (a, b) -> a * b);
    }
}
