package info.jab.aoc2025.day10;

import module java.base;

public final class Part2Solver {

    private static final long DEFAULT_MAX_LIMIT = 1000L;

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

        // Always use parallel search with thread-safe atomic
        AtomicLong bestTotal = new AtomicLong(Long.MAX_VALUE);

        if (freeVars.isEmpty()) {
            // No free variables - just check if solution exists
            search(0, freeVars, pivotColForRows, numPivots, new long[numCols], bestTotal,
                   freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);
        } else {
            // Parallelize by splitting the first free variable's search space
            executeParallelSearch(freeVars, pivotColForRows, numPivots, numCols, bestTotal,
                                 freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);
        }

        long result = bestTotal.get();
        return result == Long.MAX_VALUE ? 0 : result;
    }

    private static void executeParallelSearch(List<Integer> freeVars, int[] pivotColForRows,
                                             int numPivots, int numCols, AtomicLong bestTotal,
                                             long[][] freeCoeffNum, long[][] freeCoeffDen,
                                             long[] rhsNum, long[] rhsDen) {
        int firstFreeVar = freeVars.get(0);
        long maxLimit = DEFAULT_MAX_LIMIT;
        int numThreads = Runtime.getRuntime().availableProcessors();
        long chunkSize = Math.max(1, maxLimit / (numThreads * 2));

        try (ForkJoinPool pool = new ForkJoinPool(numThreads)) {
            // Collect all tasks to ensure they complete before pool shutdown
            List<ForkJoinTask<Long>> tasks = new ArrayList<>();

            for (long start = 0; start <= maxLimit; start += chunkSize) {
                long end = Math.min(start + chunkSize - 1, maxLimit);

                ParallelSearchTask task = new ParallelSearchTask(freeVars, pivotColForRows, numPivots,
                        numCols, bestTotal, freeCoeffNum, freeCoeffDen, rhsNum, rhsDen, firstFreeVar, start, end);
                // Submit task and collect it to await completion
                tasks.add(pool.submit(task));
            }

            // Wait for all tasks to complete before pool shutdown
            // This ensures correctness and proper parallel execution
            for (ForkJoinTask<Long> task : tasks) {
                task.join(); // Blocks until task completes
            }
        }
    }

    private static void search(int freeIdx, List<Integer> freeVars,
                       int[] pivotColForRows, int numPivots,
                       long[] currentAssignment, AtomicLong bestTotal,
                       long[][] freeCoeffNum, long[][] freeCoeffDen,
                       long[] rhsNum, long[] rhsDen) {

        // Pruning: Calculate current sum of assigned free vars
        long currentFreeSum = 0;
        for(int i=0; i<freeIdx; i++) {
            currentFreeSum += currentAssignment[freeVars.get(i)];
        }
        if (currentFreeSum >= bestTotal.get()) return;

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
            // Threshold to prevent overflow - normalize when denominator exceeds this
            // Using a smaller threshold to prevent overflow while still reducing GCD calls
            final long NORMALIZE_THRESHOLD = 1L << 20; // 2^20 (~1M)

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
                        int freeVarCol = freeVars.get(fIdx);
                        long xFree = currentAssignment[freeVarCol];

                        // Fast-path: when coeffDen == 1 (common after RREF)
                        if (coeffDen == 1) {
                            // Fast-path: when valDen == 1 too (both are integers)
                            if (valDen == 1) {
                                valNum = valNum - coeffNum * xFree;
                                // valDen remains 1
                            } else {
                                // valDen != 1, but coeffDen == 1
                                valNum = valNum - coeffNum * xFree * valDen;
                                // valDen unchanged
                                // Normalize if denominator is getting large
                                if (valDen > NORMALIZE_THRESHOLD) {
                                    long g = gcd(Math.abs(valNum), valDen);
                                    valNum /= g;
                                    valDen /= g;
                                }
                            }
                        } else {
                            // General case: val - (coeffNum/coeffDen) * x_free
                            // = (valNum/valDen) - (coeffNum * x_free) / coeffDen
                            // = (valNum * coeffDen - coeffNum * x_free * valDen) / (valDen * coeffDen)

                            // Check for potential overflow before multiplication
                            if (valDen > NORMALIZE_THRESHOLD || coeffDen > NORMALIZE_THRESHOLD) {
                                // Normalize first to prevent overflow
                                long g = gcd(Math.abs(valNum), valDen);
                                valNum /= g;
                                valDen /= g;
                            }

                            valNum = valNum * coeffDen - coeffNum * xFree * valDen;
                            valDen = valDen * coeffDen;

                            // Normalize after operation to prevent overflow in next iteration
                            if (valDen > NORMALIZE_THRESHOLD) {
                                long g = gcd(Math.abs(valNum), valDen);
                                valNum /= g;
                                valDen /= g;
                            }
                        }
                    }
                }

                // Final normalization before checking if integer
                // This is necessary to ensure valDen == 1 for integer solutions
                if (valDen != 1) {
                    long g = gcd(Math.abs(valNum), valDen);
                    valNum /= g;
                    valDen /= g;
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

                // Thread-safe update of best total
                final long finalTotalPresses = totalPresses;
                bestTotal.updateAndGet(current -> Math.min(current, finalTotalPresses));
            }
            return;
        }

        // Iterate free variable with improved bounds
        int fCol = freeVars.get(freeIdx);

        // Improved bounds: Use remaining budget to dynamically limit search space
        long maxLimit = DEFAULT_MAX_LIMIT;
        long currentBest = bestTotal.get();
        if (currentBest != Long.MAX_VALUE && currentFreeSum < currentBest) {
            // We've found at least one solution, use remaining budget
            long remainingBudget = currentBest - currentFreeSum;
            if (remainingBudget >= 0 && remainingBudget < maxLimit) {
                maxLimit = remainingBudget;
            }
        }

        for (long val = 0; val <= maxLimit; val++) {
            currentAssignment[fCol] = val;

            search(freeIdx + 1, freeVars, pivotColForRows, numPivots, currentAssignment, bestTotal,
                   freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);

            // Early termination: if we found optimal solution (0 cost), stop searching
            if (bestTotal.get() == 0) return;
        }
    }

    /**
     * Task for parallel search using ForkJoinPool.
     * Splits the search space across multiple threads when beneficial.
     */
    private static class ParallelSearchTask extends RecursiveTask<Long> {
        private final List<Integer> freeVars;
        private final int[] pivotColForRows;
        private final int numPivots;
        private final int numCols;
        private final AtomicLong bestTotal;
        private final long[][] freeCoeffNum;
        private final long[][] freeCoeffDen;
        private final long[] rhsNum;
        private final long[] rhsDen;
        private final int firstFreeVar;
        private final long startVal;
        private final long endVal;

        ParallelSearchTask(List<Integer> freeVars, int[] pivotColForRows, int numPivots,
                          int numCols, AtomicLong bestTotal,
                          long[][] freeCoeffNum, long[][] freeCoeffDen,
                          long[] rhsNum, long[] rhsDen, int firstFreeVar, long startVal, long endVal) {
            this.freeVars = freeVars;
            this.pivotColForRows = pivotColForRows;
            this.numPivots = numPivots;
            this.numCols = numCols;
            this.bestTotal = bestTotal;
            this.freeCoeffNum = freeCoeffNum;
            this.freeCoeffDen = freeCoeffDen;
            this.rhsNum = rhsNum;
            this.rhsDen = rhsDen;
            this.firstFreeVar = firstFreeVar;
            this.startVal = startVal;
            this.endVal = endVal;
        }

        @Override
        protected Long compute() {
            // Allocate array once per task - no cloning needed
            // Initialize with zeros (default for long arrays)
            long[] assignment = new long[numCols];

            // Search the assigned range for the first free variable
            for (long val = startVal; val <= endVal && bestTotal.get() != 0; val++) {
                assignment[firstFreeVar] = val;
                search(1, freeVars, pivotColForRows, numPivots, assignment,
                      bestTotal, freeCoeffNum, freeCoeffDen, rhsNum, rhsDen);
            }
            return bestTotal.get();
        }
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
