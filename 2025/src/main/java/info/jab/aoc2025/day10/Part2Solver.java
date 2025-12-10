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

        // Iterate free variable
        // We need bounds.
        // Heuristic: Input targets are up to ~300. Buttons add ~1-5 lights.
        // Max presses shouldn't exceed max target drastically.
        // But with negative interference, it could be higher?
        // Let's use a safe upper bound. 1000 is reasonable given problem constraints.

        int fCol = freeVars.get(freeIdx);

        // Try to derive bound for this variable
        long maxLimit = 1000; // Safety cap

        for (long val = 0; val <= maxLimit; val++) {
            currentAssignment[fCol] = val;

            // Check partial feasibility?
            // Only if coefficients are all positive/negative?
            // With mixed coefficients, we can't easily prune without full check.
            // But we can check if any row *already* violated?
            // Only if future free vars can't fix it.
            // If x_p = 10 - x_f1 - x_f2. If x_f1=20, x_p = -10 - x_f2. Since x_f2 >= 0, x_p <= -10. Impossible.
            // So if x_p becomes negative and all remaining coefficients for future free vars are non-negative (subtracting them), we can break.
            // In our equation: x_p = RHS - coeff * x_f.
            // If RHS - coeff*x_f < 0, and future terms are - coeff2 * x_f2...
            // It gets complicated.

            search(freeIdx + 1, freeVars, matrix, pivotColForRows, numPivots, currentAssignment, bestTotal);

            if (currentAssignment[fCol] == 0 && bestTotal[0] == 0) return; // Optimization if 0 cost possible
        }
    }
    
    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}

