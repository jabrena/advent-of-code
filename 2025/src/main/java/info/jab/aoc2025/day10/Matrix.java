package info.jab.aoc2025.day10;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Matrix {

    private final Fraction[][] data;
    private final int rows;
    private final int cols;
    private int[] pivotColForRows;
    private boolean[] isPivotCol;
    private int pivotRow;

    public Matrix(int[] targets, int[][] buttons) {
        this.rows = targets.length;
        this.cols = buttons.length;
        this.data = new Fraction[rows][cols + 1];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean affects = false;
                for (int r : buttons[j]) {
                    if (r == i) {
                        affects = true;
                        break;
                    }
                }
                data[i][j] = affects ? Fraction.ONE : Fraction.ZERO;
            }
            data[i][cols] = new Fraction(targets[i]);
        }
    }

    public void toRREF() {
        pivotRow = 0;
        pivotColForRows = new int[rows];
        Arrays.fill(pivotColForRows, -1);
        isPivotCol = new boolean[cols];

        for (int j = 0; j < cols && pivotRow < rows; j++) {
            int sel = -1;
            for (int i = pivotRow; i < rows; i++) {
                if (!data[i][j].isZero()) {
                    sel = i;
                    break;
                }
            }

            if (sel == -1) continue;

            // Swap rows
            Fraction[] temp = data[pivotRow];
            data[pivotRow] = data[sel];
            data[sel] = temp;

            // Normalize pivot row
            Fraction div = data[pivotRow][j];
            for (int k = j; k <= cols; k++) {
                data[pivotRow][k] = data[pivotRow][k].divide(div);
            }

            // Eliminate other rows
            for (int i = 0; i < rows; i++) {
                if (i != pivotRow && !data[i][j].isZero()) {
                    Fraction mul = data[i][j];
                    for (int k = j; k <= cols; k++) {
                        data[i][k] = data[i][k].subtract(mul.multiply(data[pivotRow][k]));
                    }
                }
            }

            pivotColForRows[pivotRow] = j;
            isPivotCol[j] = true;
            pivotRow++;
        }
    }

    public boolean isConsistent() {
        for (int i = pivotRow; i < rows; i++) {
            if (!data[i][cols].isZero()) {
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

    public Fraction get(int row, int col) {
        return data[row][col];
    }
    
    public Fraction getRHS(int row) {
        return data[row][cols];
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

