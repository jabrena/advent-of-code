package info.jab.aoc2025.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RationalMatrix {

    // Store as flattened arrays: [row * (cols+1) + col]
    private final long[] numerators;
    private final long[] denominators;
    private final int rows;
    private final int cols;
    private int[] pivotColForRows;
    private boolean[] isPivotCol;
    private int pivotRow;

    public RationalMatrix(int[] targets, int[][] buttons) {
        this.rows = targets.length;
        this.cols = buttons.length;
        int totalSize = rows * (cols + 1);
        this.numerators = new long[totalSize];
        this.denominators = new long[totalSize];

        // Initialize matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean affects = false;
                for (int r : buttons[j]) {
                    if (r == i) {
                        affects = true;
                        break;
                    }
                }
                int idx = index(i, j);
                numerators[idx] = affects ? 1 : 0;
                denominators[idx] = 1;
            }
            // RHS (augmented column)
            int rhsIdx = index(i, cols);
            numerators[rhsIdx] = targets[i];
            denominators[rhsIdx] = 1;
        }
    }

    private int index(int row, int col) {
        return row * (cols + 1) + col;
    }

    public void toRREF() {
        pivotRow = 0;
        pivotColForRows = new int[rows];
        Arrays.fill(pivotColForRows, -1);
        isPivotCol = new boolean[cols];

        for (int j = 0; j < cols && pivotRow < rows; j++) {
            int sel = -1;
            for (int i = pivotRow; i < rows; i++) {
                int idx = index(i, j);
                if (numerators[idx] != 0) {
                    sel = i;
                    break;
                }
            }

            if (sel == -1) continue;

            // Swap rows
            swapRows(pivotRow, sel);

            // Normalize pivot row
            int pivotIdx = index(pivotRow, j);
            long divNum = numerators[pivotIdx];
            long divDen = denominators[pivotIdx];

            if (divNum == 0) {
                throw new ArithmeticException("Pivot element is zero");
            }

            for (int k = j; k <= cols; k++) {
                int idx = index(pivotRow, k);
                // Divide: (num/den) / (divNum/divDen) = (num * divDen) / (den * divNum)
                long newNum = numerators[idx] * divDen;
                long newDen = denominators[idx] * divNum;
                normalizeAndSet(idx, newNum, newDen);
            }

            // Eliminate other rows
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow) {
                    int mulIdx = index(i, j);
                    if (numerators[mulIdx] != 0) {
                        long mulNum = numerators[mulIdx];
                        long mulDen = denominators[mulIdx];

                        for (int k = j; k <= cols; k++) {
                            int pivotIdx2 = index(pivotRow, k);
                            int targetIdx = index(i, k);

                            // Subtract: (num/den) - (mulNum/mulDen) * (pivotNum/pivotDen)
                            // = (num/den) - (mulNum * pivotNum) / (mulDen * pivotDen)
                            // = (num * mulDen * pivotDen - mulNum * pivotNum * den) / (den * mulDen * pivotDen)
                            long pivotNum = numerators[pivotIdx2];
                            long pivotDen = denominators[pivotIdx2];

                            long num = numerators[targetIdx];
                            long den = denominators[targetIdx];

                            long newNum = num * mulDen * pivotDen - mulNum * pivotNum * den;
                            long newDen = den * mulDen * pivotDen;

                            if (newDen == 0) {
                                // This should not happen if denominators are properly maintained
                                // But if it does, skip this operation
                                continue;
                            }

                            normalizeAndSet(targetIdx, newNum, newDen);
                        }
                    }
                }
            }

            pivotColForRows[pivotRow] = j;
            isPivotCol[j] = true;
            pivotRow++;
        }
    }

    private void swapRows(int row1, int row2) {
        for (int j = 0; j <= cols; j++) {
            int idx1 = index(row1, j);
            int idx2 = index(row2, j);

            long tempNum = numerators[idx1];
            long tempDen = denominators[idx1];
            numerators[idx1] = numerators[idx2];
            denominators[idx1] = denominators[idx2];
            numerators[idx2] = tempNum;
            denominators[idx2] = tempDen;
        }
    }

    private void normalizeAndSet(int idx, long num, long den) {
        if (den == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (den < 0) {
            num = -num;
            den = -den;
        }

        // Always normalize to keep fractions reduced
        // This prevents precision loss and overflow
        long g = gcd(Math.abs(num), den);
        num /= g;
        den /= g;

        numerators[idx] = num;
        denominators[idx] = den;
    }

    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    public boolean isConsistent() {
        for (int i = pivotRow; i < rows; i++) {
            int rhsIdx = index(i, cols);
            if (numerators[rhsIdx] != 0) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getFreeVariables() {
        List<Integer> freeVars = new ArrayList<>();
        for (int j = 0; j < cols; j++) {
            if (!isPivotCol[j]) {
                freeVars.add(j);
            }
        }
        return freeVars;
    }

    public long getNumerator(int row, int col) {
        return numerators[index(row, col)];
    }

    public long getDenominator(int row, int col) {
        return denominators[index(row, col)];
    }

    public long getRHSNumerator(int row) {
        return numerators[index(row, cols)];
    }

    public long getRHSDenominator(int row) {
        return denominators[index(row, cols)];
    }

    public int[] getPivotColForRows() {
        return pivotColForRows;
    }

    public int getPivotRowCount() {
        return pivotRow;
    }

    public int getCols() {
        return cols;
    }
}

