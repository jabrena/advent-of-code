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
        return lines.stream()
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
        private final int[][] matrix; // [row][col]
        private final int[] targets;
        private final int numRows;
        private final int numCols;
        private long minTotalPresses = Long.MAX_VALUE;
        private final boolean[] isAssigned;
        private final int[] assignment;

        public SolverPart2(int[][] matrix, int[] targets) {
            this.matrix = matrix;
            this.targets = targets;
            this.numRows = matrix.length;
            this.numCols = matrix[0].length;
            this.isAssigned = new boolean[numCols];
            this.assignment = new int[numCols];
        }

        public long solve() {
            backtrack(0);
            return minTotalPresses == Long.MAX_VALUE ? 0 : minTotalPresses;
        }

        private void backtrack(long currentSum) {
            if (currentSum >= minTotalPresses) {
                return;
            }

            // Check if all rows satisfied
            // And pick the best row to branch on
            int bestRow = -1;
            long minCombinations = Long.MAX_VALUE;
            
            boolean allSatisfied = true;
            
            for (int r = 0; r < numRows; r++) {
                // Calculate current sum for this row from ASSIGNED variables
                int currentVal = 0;
                List<Integer> unassignedVars = new ArrayList<>();
                for (int c = 0; c < numCols; c++) {
                    if (matrix[r][c] == 1) {
                        if (isAssigned[c]) {
                            currentVal += assignment[c];
                        } else {
                            unassignedVars.add(c);
                        }
                    }
                }
                
                int remaining = targets[r] - currentVal;
                if (remaining < 0) {
                    return; // Invalid state
                }
                
                if (unassignedVars.isEmpty()) {
                    if (remaining != 0) {
                        return; // Invalid state
                    }
                } else {
                    allSatisfied = false;
                    // Heuristic: pick row with few combinations
                    long combos = remaining + unassignedVars.size(); 
                    if (combos < minCombinations) {
                        minCombinations = combos;
                        bestRow = r;
                    }
                }
            }
            
            if (allSatisfied) {
                minTotalPresses = currentSum;
                return;
            }
            
            // Branch on bestRow
            List<Integer> vars = new ArrayList<>();
            int currentVal = 0;
            for (int c = 0; c < numCols; c++) {
                if (matrix[bestRow][c] == 1) {
                    if (isAssigned[c]) {
                        currentVal += assignment[c];
                    } else {
                        vars.add(c);
                    }
                }
            }
            int targetVal = targets[bestRow] - currentVal;
            
            generatePartitions(targetVal, vars, 0, currentSum);
        }
        
        private void generatePartitions(int target, List<Integer> vars, int varIdx, long currentSum) {
             if (currentSum >= minTotalPresses) return;

             if (varIdx == vars.size() - 1) {
                 // Last variable takes the remainder
                 int val = target;
                 int col = vars.get(varIdx);
                 
                 assignment[col] = val;
                 isAssigned[col] = true;
                 backtrack(currentSum + val);
                 isAssigned[col] = false;
                 return;
             }
             
             int col = vars.get(varIdx);
             // Try values for this variable from 0 to target
             for (int val = 0; val <= target; val++) {
                 assignment[col] = val;
                 isAssigned[col] = true;
                 generatePartitions(target - val, vars, varIdx + 1, currentSum + val);
                 isAssigned[col] = false;
             }
        }
    }
}
