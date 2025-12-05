package info.jab.aoc2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * I maintain this version because I like the idea about operators to evolve the V2 to use ADTs
 */
public class ChronospatialComputer {
    // Registers
    private int a;
    private int b;
    private int c;
    private final List<Integer> opcodes;
    private int instructionPointer;
    private final List<Integer> output;

    public static ChronospatialComputer fromInput(List<String> input) {
        List<String> lines = input.stream()
                                   .map(String::trim)
                                   .filter(line -> !line.isEmpty())
                                   .toList();

        // Parse registers
        int a = Integer.parseInt(lines.get(0).split(": ")[1]);
        int b = Integer.parseInt(lines.get(1).split(": ")[1]);
        int c = Integer.parseInt(lines.get(2).split(": ")[1]);

        // Parse program
        String programLine = lines.get(3).split(": ")[1];
        List<Integer> opcodes = Arrays.stream(programLine.split(","))
                                      .map(Integer::parseInt)
                                      .toList();

        return new ChronospatialComputer(a, b, c, opcodes);
    }

    public ChronospatialComputer(int a, int b, int c, List<Integer> opcodes) {
        this.a = a;
        this.b = b;
        this.c = c;
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

            if (opcode != 3 || a == 0) {
                instructionPointer += 2;
            }
        }
    }

    private int getComboOperandValue(int operand) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> a;
            case 5 -> b;
            case 6 -> c;
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }

    private void adv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        a = a / denominator;
    }

    private void bxl(int operand) {
        b = b ^ operand;
    }

    private void bst(int operand) {
        b = getComboOperandValue(operand) % 8;
    }

    private void jnz(int operand) {
        if (a != 0) {
            instructionPointer = operand;
        }
    }

    private void bxc() {
        b = b ^ c;
    }

    private void out(int operand) {
        output.add(getComboOperandValue(operand) % 8);
    }

    private void bdv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        b = a / denominator;
    }

    private void cdv(int operand) {
        int denominator = (int) Math.pow(2, getComboOperandValue(operand));
        c = a / denominator;
    }

    public String getOutput() {
        return String.join(",", output.stream().map(String::valueOf).toList());
    }
}

