package info.jab.aoc2024.day17;

import java.util.ArrayList;
import java.util.List;

class Computer {
    private long a;
    private long b;
    private long c;
    private final List<Integer> opcodes;

    private int ip;

    Computer(long registerA, int registerB, int registerC, List<Integer> opcodes) {
        this.a = registerA;
        this.b = registerB;
        this.c = registerC;
        this.opcodes = opcodes;
        this.ip = 0;
    }

    //TODO Improve the refactoring using ADTs
    List<Integer> execute() {
        List<Integer> out = new ArrayList<>();
        while (ip < opcodes.size()) {
            int litOp = opcodes.get(ip + 1);
            long combOp = getComboOperandValue(opcodes.get(ip + 1));

            boolean skipIncrease = false;
            switch (opcodes.get(ip)) {
                case 0 -> {
                    long den = (long) Math.pow(2, combOp);
                    a = a / den;
                }
                case 1 -> b = b ^ litOp;
                case 2 -> b = combOp % 8;
                case 3 -> {
                    if (a != 0) {
                        ip = litOp;
                        skipIncrease = true;
                    } else {
                        skipIncrease = false;
                    }
                }
                case 4 -> b = b ^ c;
                case 5 -> out.add((int) (combOp % 8L));
                case 6 -> {
                    long den = (long) Math.pow(2, combOp);
                    b = a / den;
                }
                case 7 -> {
                    long den = (long) Math.pow(2, combOp);
                    c = a / den;
                }
                default -> {
                    // Unknown opcode, skip instruction
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
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + value);
        };
    }

    String print(List<Integer> out) {
        return String.join(",", out.stream().map(Long::toString).toList());
    }
}

