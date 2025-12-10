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

        // Precompute coefficient matrices for faster access in search loop
        // freeCoeffNum[pivotRow][freeVarIdx] = coefficient numerator
        // freeCoeffDen[pivotRow][freeVarIdx] = coefficient denominator
        long[][] freeCoeffNum = new long[numPivots][freeVars.size()];
        long[][] freeCoeffDen = new long[numPivots][freeVars.size()];
        long[] rhsNum = new long[numPivots];
        long[] rhsDen = new long[numPivots];

        for (int i = 0; i < numPivots; i++) {
            rhsNum[i] = matrix.getRHSNumerator(i);
            rhsDen[i] = matrix.getRHSDenominator(i);
            for (int fIdx = 0; fIdx < freeVars.size(); fIdx++) {
                int freeVarCol = freeVars.get(fIdx);
                freeCoeffNum[i][fIdx] = matrix.getNumerator(i, freeVarCol);
                freeCoeffDen[i][fIdx] = matrix.getDenominator(i, freeVarCol);
            }
        }

        // Solve
        // We need to pick non-negative integers for free variables such that pivot variables are non-negative integers.
        // And minimize sum.

        long[] bestTotal = {Long.MAX_VALUE};
        long[] currentAssignment = new long[numCols];

        search(0, freeVars, pivotColForRows, numPivots, currentAssignment, bestTotal,
               freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);

        return bestTotal[0] == Long.MAX_VALUE ? 0 : bestTotal[0];
    }

    private void search(int freeIdx, List<Integer> freeVars,
                       int[] pivotColForRows, int numPivots,
                       long[] currentAssignment, long[] bestTotal,
                       long[][] freeCoeffNum, long[][] freeCoeffDen,
                       long[] rhsNum, long[] rhsDen) {

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

                // Get RHS from precomputed arrays
                long valNum = rhsNum[i];
                long valDen = rhsDen[i];

                // Subtract: val - sum(coeff * x_free)
                // Use precomputed coefficients for faster access
                for (int fIdx = 0; fIdx < freeVars.size(); fIdx++) {
                    long coeffNum = freeCoeffNum[i][fIdx];
                    long coeffDen = freeCoeffDen[i][fIdx];

                    if (coeffNum != 0) {
                        // val - (coeffNum/coeffDen) * x_free
                        // = (valNum/valDen) - (coeffNum * x_free) / coeffDen
                        // = (valNum * coeffDen - coeffNum * x_free * valDen) / (valDen * coeffDen)
                        int freeVarCol = freeVars.get(fIdx);
                        long xFree = currentAssignment[freeVarCol];
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

        // Improved bounds: Use remaining budget to dynamically limit search space
        // As we find better solutions, the search space shrinks
        long maxLimit = 1000L; // Default safety cap
        if (bestTotal[0] != Long.MAX_VALUE && currentFreeSum < bestTotal[0]) {
            // We've found at least one solution, use remaining budget
            long remainingBudget = bestTotal[0] - currentFreeSum;
            if (remainingBudget >= 0 && remainingBudget < maxLimit) {
                maxLimit = remainingBudget;
            }
        }

        for (long val = 0; val <= maxLimit; val++) {
            currentAssignment[fCol] = val;

            search(freeIdx + 1, freeVars, pivotColForRows, numPivots, currentAssignment, bestTotal,
                   freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);

            // Early termination: if we found optimal solution (0 cost), stop searching
            if (bestTotal[0] == 0) return;
        }
    }


    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

