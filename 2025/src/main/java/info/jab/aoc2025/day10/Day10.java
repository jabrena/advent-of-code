package info.jab.aoc2025.day10;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 implements Day<Long> {

    @Override
    public Long getPart1Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return lines.stream()
            .filter(line -> line.contains("["))
            .mapToLong(this::solveMachine)
            .sum();
    }

    @Override
    public Long getPart2Result(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        return lines.parallelStream()
            .filter(line -> line.contains("["))
            .mapToLong(this::solveMachinePart2)
            .sum();
    }

    private long solveMachine(String line) {
        // Parse line
        int openBracket = line.indexOf('[');
        int closeBracket = line.indexOf(']');
        String diagram = line.substring(openBracket + 1, closeBracket);

        long target = 0;
        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) == '#') {
                target |= (1L << i);
            }
        }

        int openBrace = line.indexOf('{');
        String buttonsPart = line.substring(closeBracket + 1, openBrace).trim();

        List<Long> buttons = new ArrayList<>();
        Pattern p = Pattern.compile("\\(([0-9,]+)\\)");
        Matcher m = p.matcher(buttonsPart);
        while (m.find()) {
            String content = m.group(1);
            long buttonMask = 0;
            String[] parts = content.split(",");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    int bit = Integer.parseInt(part);
                    buttonMask |= (1L << bit);
                }
            }
            buttons.add(buttonMask);
        }

        return findMinPresses(target, buttons);
    }

    private long findMinPresses(long target, List<Long> buttons) {
        int n = buttons.size();
        long minPresses = Long.MAX_VALUE;

        long limit = 1L << n;
        for (long i = 0; i < limit; i++) {
            long current = 0;
            int presses = 0;
            for (int j = 0; j < n; j++) {
                if ((i & (1L << j)) != 0) {
                    current ^= buttons.get(j);
                    presses++;
                }
            }

            if (current == target) {
                if (presses < minPresses) {
                    minPresses = presses;
                }
            }
        }

        return minPresses == Long.MAX_VALUE ? 0 : minPresses;
    }

    private long solveMachinePart2(String line) {
        // Parse { ... }
        int openBrace = line.indexOf('{');
        int closeBrace = line.indexOf('}');
        String joltagePart = line.substring(openBrace + 1, closeBrace);
        String[] joltageStrs = joltagePart.split(",");
        int[] targets = new int[joltageStrs.length];
        for (int i = 0; i < joltageStrs.length; i++) {
            targets[i] = Integer.parseInt(joltageStrs[i].trim());
        }

        // Parse (...) buttons
        int closeBracket = line.indexOf(']');
        int openBraceIdx = line.indexOf('{');
        String buttonsPart = line.substring(closeBracket + 1, openBraceIdx).trim();
        List<List<Integer>> buttons = new ArrayList<>();
        Pattern p = Pattern.compile("\\(([0-9,]+)\\)");
        Matcher m = p.matcher(buttonsPart);
        while (m.find()) {
            String content = m.group(1);
            List<Integer> button = new ArrayList<>();
            String[] parts = content.split(",");
            for (String part : parts) {
                if (!part.isEmpty()) {
                    button.add(Integer.parseInt(part.trim()));
                }
            }
            buttons.add(button);
        }

        // Convert to matrix format: rows = counters, cols = buttons
        // matrix[row][col] = 1 if button col affects counter row
        int numRows = targets.length;
        int numCols = buttons.size();
        int[][] matrix = new int[numRows][numCols];
        for (int j = 0; j < numCols; j++) {
            for (int r : buttons.get(j)) {
                if (r < numRows) {
                    matrix[r][j] = 1;
                }
            }
        }

        return new SolverPart2(matrix, targets).solve();
    }

    private static class SolverPart2 {
        private final long[] rowMasks; // row -> bitmask of columns
        private final long[] colMasks; // col -> bitmask of rows
        private final int[] targets;
        private final int numRows;
        private final int numCols;
        private long best = Long.MAX_VALUE;

        public SolverPart2(int[][] matrix, int[] targets) {
            this.numRows = matrix.length;
            this.numCols = matrix[0].length;
            this.targets = targets;

            this.rowMasks = new long[numRows];
            this.colMasks = new long[numCols];

            for (int r = 0; r < numRows; r++) {
                for (int c = 0; c < numCols; c++) {
                    if (matrix[r][c] == 1) {
                        rowMasks[r] |= (1L << c);
                        colMasks[c] |= (1L << r);
                    }
                }
            }
        }

        public long solve() {
            long activeCols = (1L << numCols) - 1;
            dfs(activeCols, targets.clone(), 0);
            return best == Long.MAX_VALUE ? 0 : best;
        }

        private void dfs(long activeCols, int[] currentTargets, long currentPresses) {
            if (currentPresses >= best) {
                return;
            }

            // 1. Singleton Propagation
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int r = 0; r < numRows; r++) {
                    if (currentTargets[r] < 0) {
                        return; // Invalid
                    }
                    if (currentTargets[r] > 0) {
                        long avail = rowMasks[r] & activeCols;
                        if (avail == 0) {
                            return; // Impossible
                        }
                        if (Long.bitCount(avail) == 1) {
                            int col = Long.numberOfTrailingZeros(avail);
                            int val = currentTargets[r]; // Must use this col to satisfy this row

                            if (currentPresses + val >= best) {
                                return;
                            }

                            activeCols &= ~(1L << col);
                            long rowsAffected = colMasks[col];
                            long tempRows = rowsAffected;
                            while (tempRows != 0) {
                                int k = Long.numberOfTrailingZeros(tempRows);
                                tempRows &= ~(1L << k);
                                currentTargets[k] -= val;
                                if (currentTargets[k] < 0) {
                                    return;
                                }
                            }
                            currentPresses += val;
                            changed = true;
                        }
                    }
                }
            }

            // 2. Check Solved
            boolean allSatisfied = true;
            for (int t : currentTargets) {
                if (t != 0) {
                    allSatisfied = false;
                    break;
                }
            }
            if (allSatisfied) {
                best = currentPresses;
                return;
            }

            if (activeCols == 0) {
                return;
            }

            // 3. Lower Bound Pruning
            long sumRemaining = 0;
            for (int t : currentTargets) {
                sumRemaining += t;
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
                if (currentPresses + minNeeded >= best) {
                    return;
                }
            } else if (sumRemaining > 0) {
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
            for (int val = 0; val <= limit; val++) {
                if (currentPresses + val >= best) {
                    break;
                }

                int[] nextTargets = currentTargets.clone();
                tempRows = rowsAffected;
                while (tempRows != 0) {
                    int k = Long.numberOfTrailingZeros(tempRows);
                    tempRows &= ~(1L << k);
                    nextTargets[k] -= val;
                }

                dfs(nextActive, nextTargets, currentPresses + val);
            }
        }
    }
}
