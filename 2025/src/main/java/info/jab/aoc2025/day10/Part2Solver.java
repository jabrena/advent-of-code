package info.jab.aoc2025.day10;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public final class Part2Solver {

    private static final VectorSpecies<Integer> SPECIES = IntVector.SPECIES_PREFERRED;

    public long solve(Part2Problem problem) {
        int[] targets = problem.targets().clone(); // Clone to avoid modifying the input record

        int[][] buttons = problem.buttons();
        int numRows = targets.length;
        int numCols = buttons.length;

        long[] rowMasks = new long[numRows];
        long[] colMasks = new long[numCols];

        for (int j = 0; j < numCols; j++) {
            int[] button = buttons[j];
            for (int r : button) {
                if (r < numRows) {
                    rowMasks[r] |= (1L << j);
                    colMasks[j] |= (1L << r);
                }
            }
        }

        long[] bestHolder = {Long.MAX_VALUE};
        long activeCols = (1L << numCols) - 1;

        // Preallocate buffers for recursion
        // Max depth is numCols (since we remove at least 1 col per step)
        int[][] propColsStack = new int[numCols + 1][numCols];
        int[][] propValsStack = new int[numCols + 1][numCols];

        dfsPart2(activeCols, targets, 0, rowMasks, colMasks, bestHolder, 0, propColsStack, propValsStack);

        return bestHolder[0] == Long.MAX_VALUE ? 0 : bestHolder[0];
    }

    private void dfsPart2(long activeCols, int[] currentTargets, long currentPresses,
                         long[] rowMasks, long[] colMasks, long[] bestHolder,
                         int depth, int[][] propColsStack, int[][] propValsStack) {
        if (currentPresses >= bestHolder[0]) {
            return;
        }

        int numRows = rowMasks.length;

        // History for backtracking singleton propagation
        // Use preallocated stack buffers to avoid allocation
        int[] propCols = propColsStack[depth];
        int[] propVals = propValsStack[depth];
        int propCount = 0;

        // 1. Singleton Propagation
        boolean changed = true;
        boolean possible = true;

        while (changed && possible) {
            changed = false;
            for (int r = 0; r < numRows; r++) {
                if (currentTargets[r] < 0) {
                    possible = false;
                    break;
                }
                if (currentTargets[r] > 0) {
                    long avail = rowMasks[r] & activeCols;
                    if (avail == 0) {
                        possible = false; // Impossible
                        break;
                    }
                    if (Long.bitCount(avail) == 1) {
                        int col = Long.numberOfTrailingZeros(avail);
                        int val = currentTargets[r]; // Must use this col to satisfy this row

                        if (currentPresses + val >= bestHolder[0]) {
                            possible = false;
                            break;
                        }

                        // Record change for undo
                        propCols[propCount] = col;
                        propVals[propCount] = val;
                        propCount++;

                        activeCols &= ~(1L << col);
                        long rowsAffected = colMasks[col];
                        while (rowsAffected != 0) {
                            int k = Long.numberOfTrailingZeros(rowsAffected);
                            rowsAffected &= ~(1L << k);
                            currentTargets[k] -= val;
                            if (currentTargets[k] < 0) {
                                possible = false;
                            }
                        }
                        if (!possible) break;

                        currentPresses += val;
                        changed = true;
                    }
                }
            }
        }

        if (!possible) {
            undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
            return;
        }

        // 2. Check Solved
        boolean allSatisfied = true;
        int i = 0;
        int vectorLimit = SPECIES.loopBound(currentTargets.length);
        for (; i < vectorLimit; i += SPECIES.length()) {
            IntVector v = IntVector.fromArray(SPECIES, currentTargets, i);
            if (v.reduceLanes(VectorOperators.OR) != 0) {
                allSatisfied = false;
                break;
            }
        }
        if (allSatisfied) {
            for (; i < currentTargets.length; i++) {
                if (currentTargets[i] != 0) {
                    allSatisfied = false;
                    break;
                }
            }
        }

        if (allSatisfied) {
            bestHolder[0] = currentPresses;
            undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
            return;
        }

        if (activeCols == 0) {
            undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
            return;
        }

        // 3. Lower Bound Pruning
        long sumRemaining = 0;
        int maxTarget = 0;

        i = 0;
        for (; i < vectorLimit; i += SPECIES.length()) {
            IntVector v = IntVector.fromArray(SPECIES, currentTargets, i);
            // reduceLanes(ADD) wraps on overflow, but maxTarget is safe.
            // For sum, we accumulate to long to be safe from total overflow,
            // but lane overflow is still possible if elements are > 2B.
            // Assuming elements are modest as per problem domain.
            sumRemaining += v.reduceLanes(VectorOperators.ADD);
            maxTarget = Math.max(maxTarget, v.reduceLanes(VectorOperators.MAX));
        }

        for (; i < currentTargets.length; i++) {
            int t = currentTargets[i];
            sumRemaining += t;
            if (t > maxTarget) {
                maxTarget = t;
            }
        }

        if (currentPresses + maxTarget >= bestHolder[0]) {
             undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
             return;
        }

        int maxCov = 0;
        long temp = activeCols;
        while (temp != 0) {
            int c = Long.numberOfTrailingZeros(temp);
            temp &= ~(1L << c);
            maxCov = Math.max(maxCov, Long.bitCount(colMasks[c]));
        }

        if (maxCov > 0) {
            long minNeeded = (sumRemaining + maxCov - 1) / maxCov;
            if (currentPresses + minNeeded >= bestHolder[0]) {
                undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
                return;
            }
        } else if (sumRemaining > 0) {
            undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
            return;
        }

        // 4. Branching
        // Heuristic: Pick row with fewest active columns
        int minDegree = Integer.MAX_VALUE;
        int bestRow = -1;

        for (int r = 0; r < numRows; r++) {
            if (currentTargets[r] > 0) {
                long avail = rowMasks[r] & activeCols;
                int deg = Long.bitCount(avail);
                if (deg < minDegree) {
                    minDegree = deg;
                    bestRow = r;
                }
            }
        }

        if (bestRow == -1) {
            undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
            return;
        }

        // Pick a column from bestRow with highest coverage
        long avail = rowMasks[bestRow] & activeCols;
        int bestCol = -1;
        int maxColCov = -1;

        long temp2 = avail;
        while (temp2 != 0) {
            int c = Long.numberOfTrailingZeros(temp2);
            temp2 &= ~(1L << c);
            int cov = Long.bitCount(colMasks[c]);
            if (cov > maxColCov) {
                maxColCov = cov;
                bestCol = c;
            }
        }

        int col = bestCol;

        // Max value is limited by targets of all affected rows
        int limit = Integer.MAX_VALUE;
        long rowsAffected = colMasks[col];

        long tempRows = rowsAffected;
        while (tempRows != 0) {
            int k = Long.numberOfTrailingZeros(tempRows);
            tempRows &= ~(1L << k);
            limit = Math.min(limit, currentTargets[k]);
        }

        long nextActive = activeCols & ~(1L << col);

        // Iterate val from 0 to limit
        int subtractedTimes = 0;

        for (int val = 0; val <= limit; val++) {
            if (currentPresses + val >= bestHolder[0]) {
                break;
            }

            if (val > 0) {
                // Incremental subtract
                tempRows = rowsAffected;
                while (tempRows != 0) {
                    int k = Long.numberOfTrailingZeros(tempRows);
                    tempRows &= ~(1L << k);
                    currentTargets[k]--;
                }
                subtractedTimes++;
            }

            dfsPart2(nextActive, currentTargets, currentPresses + val, rowMasks, colMasks, bestHolder, depth + 1, propColsStack, propValsStack);
        }

        // Backtrack the loop changes
        if (subtractedTimes > 0) {
            tempRows = rowsAffected;
            while (tempRows != 0) {
                int k = Long.numberOfTrailingZeros(tempRows);
                tempRows &= ~(1L << k);
                currentTargets[k] += subtractedTimes;
            }
        }

        undoPropagation(currentTargets, colMasks, propCols, propVals, propCount);
    }

    private void undoPropagation(int[] currentTargets, long[] colMasks,
                               int[] propCols, int[] propVals, int propCount) {
        for (int i = propCount - 1; i >= 0; i--) {
            int col = propCols[i];
            int val = propVals[i];
            long rowsAffected = colMasks[col];
            while (rowsAffected != 0) {
                int k = Long.numberOfTrailingZeros(rowsAffected);
                rowsAffected &= ~(1L << k);
                currentTargets[k] += val;
            }
        }
    }
}

