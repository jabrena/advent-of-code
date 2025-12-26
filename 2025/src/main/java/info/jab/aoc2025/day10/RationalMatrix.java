package info.jab.aoc2025.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RationalMatrix {

    // Interleaved memory layout: [num0, den0, num1, den1, ...]
    // Better cache locality - numerator and denominator are adjacent in memory
    // Index calculation: baseIdx = row * (cols+1) + col, then data[baseIdx*2] = num, data[baseIdx*2+1] = den
    private final long[] data;
    private final int rows;
    private final int cols;
    private int[] pivotColForRows;
    private boolean[] isPivotCol;
    private int pivotRow;

    public RationalMatrix(int[] targets, int[][] buttons) {
        this.rows = targets.length;
        this.cols = buttons.length;
        int totalSize = rows * (cols + 1);
        // Interleaved: each element needs 2 slots (num, den)
        this.data = new long[totalSize * 2];

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
                int baseIdx = index(i, j);
                data[baseIdx * 2] = affects ? 1 : 0;     // numerator
                data[baseIdx * 2 + 1] = 1;                // denominator
            }
            // RHS (augmented column)
            int rhsBaseIdx = index(i, cols);
            data[rhsBaseIdx * 2] = targets[i];            // numerator
            data[rhsBaseIdx * 2 + 1] = 1;                 // denominator
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
                int baseIdx = index(i, j);
                if (data[baseIdx * 2] != 0) {  // Check numerator
                    sel = i;
                    break;
                }
            }

            if (sel == -1) continue;

            // Swap rows
            swapRows(pivotRow, sel);

            // Normalize pivot row
            int pivotBaseIdx = index(pivotRow, j);
            long divNum = data[pivotBaseIdx * 2];      // numerator
            long divDen = data[pivotBaseIdx * 2 + 1];  // denominator

            if (divNum == 0) {
                throw new ArithmeticException("Pivot element is zero");
            }

            for (int k = j; k <= cols; k++) {
                int baseIdx = index(pivotRow, k);
                // Divide: (num/den) / (divNum/divDen) = (num * divDen) / (den * divNum)
                long num = data[baseIdx * 2];
                long den = data[baseIdx * 2 + 1];
                long newNum = num * divDen;
                long newDen = den * divNum;
                normalizeAndSet(baseIdx, newNum, newDen);
            }

            // Eliminate other rows
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow) {
                    int mulBaseIdx = index(i, j);
                    if (data[mulBaseIdx * 2] != 0) {  // Check numerator
                        long mulNum = data[mulBaseIdx * 2];
                        long mulDen = data[mulBaseIdx * 2 + 1];

                        for (int k = j; k <= cols; k++) {
                            int pivotBaseIdx2 = index(pivotRow, k);
                            int targetBaseIdx = index(i, k);

                            // Subtract: (num/den) - (mulNum/mulDen) * (pivotNum/pivotDen)
                            // = (num/den) - (mulNum * pivotNum) / (mulDen * pivotDen)
                            // = (num * mulDen * pivotDen - mulNum * pivotNum * den) / (den * mulDen * pivotDen)
                            long pivotNum = data[pivotBaseIdx2 * 2];
                            long pivotDen = data[pivotBaseIdx2 * 2 + 1];

                            long num = data[targetBaseIdx * 2];
                            long den = data[targetBaseIdx * 2 + 1];

                            long newNum = num * mulDen * pivotDen - mulNum * pivotNum * den;
                            long newDen = den * mulDen * pivotDen;

                            if (newDen == 0) {
                                // This should not happen if denominators are properly maintained
                                // But if it does, skip this operation
                                continue;
                            }

                            normalizeAndSet(targetBaseIdx, newNum, newDen);
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
            int baseIdx1 = index(row1, j);
            int baseIdx2 = index(row2, j);

            // Swap both numerator and denominator (adjacent in interleaved layout)
            long tempNum = data[baseIdx1 * 2];
            long tempDen = data[baseIdx1 * 2 + 1];
            data[baseIdx1 * 2] = data[baseIdx2 * 2];
            data[baseIdx1 * 2 + 1] = data[baseIdx2 * 2 + 1];
            data[baseIdx2 * 2] = tempNum;
            data[baseIdx2 * 2 + 1] = tempDen;
        }
    }

    private void normalizeAndSet(int baseIdx, long num, long den) {
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

        // Store in interleaved layout
        data[baseIdx * 2] = num;         // numerator
        data[baseIdx * 2 + 1] = den;     // denominator
    }

    /**
     * Iterative GCD implementation for better performance (avoids recursion overhead).
     */
    private static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return Math.abs(a);
    }

    public boolean isConsistent() {
        for (int i = pivotRow; i < rows; i++) {
            int rhsBaseIdx = index(i, cols);
            if (data[rhsBaseIdx * 2] != 0) {  // Check numerator
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
        int baseIdx = index(row, col);
        return data[baseIdx * 2];
    }

    public long getDenominator(int row, int col) {
        int baseIdx = index(row, col);
        return data[baseIdx * 2 + 1];
    }

    public long getRHSNumerator(int row) {
        int baseIdx = index(row, cols);
        return data[baseIdx * 2];
    }

    public long getRHSDenominator(int row) {
        int baseIdx = index(row, cols);
        return data[baseIdx * 2 + 1];
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

