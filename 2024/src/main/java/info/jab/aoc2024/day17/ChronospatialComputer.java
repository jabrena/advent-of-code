package info.jab.aoc2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * I maintain this version because I like the idea about operators to evolve the V2 to use ADTs
 */
public class ChronospatialComputer {
    // Registers
    private int A, B, C;
    private final List<Integer> opcodes;
    private int instructionPointer;
    private final List<Integer> output;

    public static ChronospatialComputer fromInput(List<String> input) {
        List<String> lines = input.stream()
                                   .map(String::trim)
                                   .filter(line -> !line.isEmpty())
                                   .toList();

        // Parse registers
        int A = Integer.parseInt(lines.get(0).split(": ")[1]);
        int B = Integer.parseInt(lines.get(1).split(": ")[1]);
        int C = Integer.parseInt(lines.get(2).split(": ")[1]);

        // Parse program
        String programLine = lines.get(3).split(": ")[1];
        List<Integer> opcodes = Arrays.stream(programLine.split(","))
                                      .map(Integer::parseInt)
                                      .toList();

        return new ChronospatialComputer(A, B, C, opcodes);
    }

    public ChronospatialComputer(int A, int B, int C, List<Integer> opcodes) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.opcodes = opcodes;

        this.instructionPointer = 0;
        this.output = new ArrayList<>();
    }

    public void run() {
        while (instructionPointer < opcodes.size()) {
            int opcode = opcodes.get(instructionPointer);
            if (instructionPointer + 1 >= opcodes.size()) break;
            int operand = opcodes.get(instructionPointer + 1);

            switch (opcode) {
                case 0 -> adv(operand);
                case 1 -> bxl(operand);
                case 2 -> bst(operand);
                case 3 -> jnz(operand);
                case 4 -> bxc();
                case 5 -> out(operand);
                case 6 -> bdv(operand);
                case 7 -> cdv(operand);
                default -> throw new IllegalArgumentException("Invalid opcode: " + opcode);
            }

            if (opcode != 3 || A == 0) {
                instructionPointer += 2;
            }
        }
    }

    private int getComboOperandValue(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> A;
            case 5 -> B;
            case 6 -> C;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }

    private void adv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        A = A / denominator;
    }

    private void bxl(int operand) {
        B = B ^ operand;
    }

    private void bst(int operand) {
        B = getComboOperandValue(operand) % 8;
    }

    private void jnz(int operand) {
        if (A != 0) {
            instructionPointer = operand;
        }
    }

    private void bxc() {
        B = B ^ C;
    }

    private void out(int operand) {
        output.add(getComboOperandValue(operand) % 8);
    }

    private void bdv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        B = A / denominator;
    }

    private void cdv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        C = A / denominator;
    }

    public String getOutput() {
        return String.join(",", output.stream().map(String::valueOf).toList());
    }
}

