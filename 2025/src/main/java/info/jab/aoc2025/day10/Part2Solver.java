package info.jab.aoc2025.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Part2Solver {

    public long solve(Part2Problem problem) {
        int[] targets = problem.targets();
        int[][] buttons = problem.buttons();

        int rows = targets.length;
        int cols = buttons.length;

        // Convert to Fraction matrix [rows][cols+1] (augmented)
        Fraction[][] matrix = new Fraction[rows][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Determine if button j affects target i
                boolean affects = false;
                for (int r : buttons[j]) {
                    if (r == i) {
                        affects = true;
                        break;
                    }
                }
                matrix[i][j] = affects ? Fraction.ONE : Fraction.ZERO;
            }
            matrix[i][cols] = new Fraction(targets[i]);
        }

        // Gaussian Elimination to RREF
        int pivotRow = 0;
        int[] pivotColForRows = new int[rows];
        Arrays.fill(pivotColForRows, -1);
        boolean[] isPivotCol = new boolean[cols];

        for (int j = 0; j < cols && pivotRow < rows; j++) {
            // Find pivot
            int sel = -1;
            for (int i = pivotRow; i < rows; i++) {
                if (!matrix[i][j].isZero()) {
                    sel = i;
                    break;
                }
            }

            if (sel == -1) continue;

            // Swap rows
            Fraction[] temp = matrix[pivotRow];
            matrix[pivotRow] = matrix[sel];
            matrix[sel] = temp;

            // Normalize pivot row
            Fraction div = matrix[pivotRow][j];
            for (int k = j; k <= cols; k++) {
                matrix[pivotRow][k] = matrix[pivotRow][k].divide(div);
            }

            // Eliminate other rows
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow && !matrix[i][j].isZero()) {
                    Fraction mul = matrix[i][j];
                    for (int k = j; k <= cols; k++) {
                        matrix[i][k] = matrix[i][k].subtract(mul.multiply(matrix[pivotRow][k]));
                    }
                }
            }

            pivotColForRows[pivotRow] = j;
            isPivotCol[j] = true;
            pivotRow++;
        }

        // Check consistency for zero rows
        for (int i = pivotRow; i < rows; i++) {
            if (!matrix[i][cols].isZero()) {
                return 0; // Impossible
            }
        }

        // Identify free variables
        List<Integer> freeVars = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            if (!isPivotCol[j]) {
                freeVars.add(j);
            }
        }

        // Solve
        // We need to pick non-negative integers for free variables such that pivot variables are non-negative integers.
        // And minimize sum.

        long[] bestTotal = {Long.MAX_VALUE};
        long[] currentAssignment = new long[cols];

        search(0, freeVars, matrix, pivotColForRows, pivotRow, currentAssignment, bestTotal);

        return bestTotal[0] == Long.MAX_VALUE ? 0 : bestTotal[0];
    }

    private void search(int freeIdx, List<Integer> freeVars, Fraction[][] matrix,
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
                Fraction val = matrix[i][matrix[0].length - 1]; // RHS

                for (int fIdx : freeVars) {
                    Fraction coeff = matrix[i][fIdx];
                    if (!coeff.isZero()) {
                        val = val.subtract(coeff.multiply(new Fraction(currentAssignment[fIdx])));
                    }
                }

                if (!val.isInteger() || val.numerator() < 0) {
                    possible = false;
                    break;
                }
                long pVal = val.numerator() / val.denominator();
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
}
