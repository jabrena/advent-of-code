package info.jab.aoc2024.day17;

import java.util.ArrayList;
import java.util.List;

class Computer {
    private long A;
    private long B;
    private long C;
    private final List<Integer> opcodes;

    private int ip;

    Computer(long registerA, int registerB, int registerC, List<Integer> opcodes) {
        this.A = registerA;
        this.B = registerB;
        this.C = registerC;
        this.opcodes = opcodes;
        this.ip = 0;
    }

    //TODO Improve the refactoring using ADTs
    List<Integer> execute(List<Integer> expected) {
        List<Integer> out = new ArrayList<>();
        while (ip < opcodes.size()) {
            int litOp = opcodes.get(ip + 1);
            long combOp = getComboOperandValue(opcodes.get(ip + 1));

            boolean skipIncrease = false;
            switch (opcodes.get(ip)) {
                case 0 -> {
                    long den = (long) Math.pow(2, combOp);
                    A = A / den;
                }
                case 1 -> B = B ^ ((long) litOp);
                case 2 -> B = combOp % 8;
                case 3 -> {
                    if (A != 0) {
                        ip = litOp;
                        skipIncrease = true;
                    }
                }
                case 4 -> B = B ^ C;
                case 5 -> out.add((int) (combOp % 8L));
                case 6 -> {
                    long den = (long) Math.pow(2, combOp);
                    B = A / den;
                }
                case 7 -> {
                    long den = (long) Math.pow(2, combOp);
                    C = A / den;
                }
            }

            if (!skipIncrease) {
                ip += 2;
            }
        }
        return out;
    }

    long getComboOperandValue(int value) {
        return switch (value % 8) {
            case 0, 1, 2, 3 -> value;
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + value);
        };
    }

    String print(List<Integer> out) {
        return String.join(",", out.stream().map(Long::toString).toList());
    }
}

