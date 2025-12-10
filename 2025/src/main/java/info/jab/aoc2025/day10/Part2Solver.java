package info.jab.aoc2025.day10;

import java.util.List;

public final class Part2Solver {

    public long solve(Part2Problem problem) {
        RationalMatrix matrix = new RationalMatrix(problem.targets(), problem.buttons());
        matrix.toRREF();

        if (!matrix.isConsistent()) {
            return 0; // Impossible
        }

        List<Integer> freeVars = matrix.getFreeVariables();
        int[] pivotColForRows = matrix.getPivotColForRows();
        int numPivots = matrix.getPivotRowCount();
        int numCols = matrix.getCols();

        // Solve
        // We need to pick non-negative integers for free variables such that pivot variables are non-negative integers.
        // And minimize sum.

        long[] bestTotal = {Long.MAX_VALUE};
        long[] currentAssignment = new long[numCols];

        search(0, freeVars, matrix, pivotColForRows, numPivots, currentAssignment, bestTotal);

        return bestTotal[0] == Long.MAX_VALUE ? 0 : bestTotal[0];
    }

    private void search(int freeIdx, List<Integer> freeVars, RationalMatrix matrix,
                       int[] pivotColForRows, int numPivots,
                       long[] currentAssignment, long[] bestTotal) {

        // Pruning: Calculate current sum of assigned free vars
        long currentFreeSum = 0;
        for(int i=0; i<freeIdx; i++) {
            currentFreeSum += currentAssignment[freeVars.get(i)];
        }
        if (currentFreeSum >= bestTotal[0]) return;

        if (freeIdx == freeVars.size()) {
            // Calculate pivots
            long totalPresses = 0;

            // Re-calculate pivots based on free vars
            // x_pivot = b' - sum(coeff * x_free)
            // But we have the matrix.
            // Row i corresponds to pivot variable pivotColForRows[i].
            // matrix[i][pivot] is 1.
            // matrix[i][free] is coeff.
            // x_pivot + sum(coeff * x_free) = rhs
            // x_pivot = rhs - sum(coeff * x_free)

            boolean possible = true;
            for (int i = 0; i < numPivots; i++) {
                int pCol = pivotColForRows[i];

                // Get RHS as rational
                long valNum = matrix.getRHSNumerator(i);
                long valDen = matrix.getRHSDenominator(i);

                // Subtract: val - sum(coeff * x_free)
                for (int fIdx : freeVars) {
                    long coeffNum = matrix.getNumerator(i, fIdx);
                    long coeffDen = matrix.getDenominator(i, fIdx);

                    if (coeffNum != 0) {
                        // val - (coeffNum/coeffDen) * x_free
                        // = (valNum/valDen) - (coeffNum * x_free) / coeffDen
                        // = (valNum * coeffDen - coeffNum * x_free * valDen) / (valDen * coeffDen)
                        long xFree = currentAssignment[fIdx];
                        valNum = valNum * coeffDen - coeffNum * xFree * valDen;
                        valDen = valDen * coeffDen;

                        // Always normalize to keep fractions reduced
                        long g = gcd(Math.abs(valNum), valDen);
                        valNum /= g;
                        valDen /= g;
                    }
                }

                // Check if integer and non-negative
                if (valDen != 1 || valNum < 0) {
                    possible = false;
                    break;
                }
                long pVal = valNum;
                currentAssignment[pCol] = pVal;
                totalPresses += pVal;
            }

            if (possible) {
                // Add free vars
                for(int f : freeVars) totalPresses += currentAssignment[f];

                if (totalPresses < bestTotal[0]) {
                    bestTotal[0] = totalPresses;
                }
            }
            return;
        }

        // Iterate free variable with improved bounds
        int fCol = freeVars.get(freeIdx);

        // Improved pruning: Use remaining budget to dynamically limit search space
        // As we find better solutions, the search space shrinks
        long maxLimit = 1000L; // Default safety cap
        if (bestTotal[0] != Long.MAX_VALUE && currentFreeSum < bestTotal[0]) {
            // We've found at least one solution, use remaining budget
            long remainingBudget = bestTotal[0] - currentFreeSum;
            if (remainingBudget < maxLimit) {
                maxLimit = remainingBudget;
            }
        }

        for (long val = 0; val <= maxLimit; val++) {
            currentAssignment[fCol] = val;

            search(freeIdx + 1, freeVars, matrix, pivotColForRows, numPivots, currentAssignment, bestTotal);

            // Early termination: if we found optimal solution (0 cost), stop searching
            if (bestTotal[0] == 0) return;
        }
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

