package info.jab.aoc2016.day12;

import info.jab.aoc.Solver;
import info.jab.aoc2016.day12.instructions.Cpy;
import info.jab.aoc2016.day12.instructions.Dec;
import info.jab.aoc2016.day12.instructions.Inc;
import info.jab.aoc2016.day12.instructions.Instruction;
import info.jab.aoc2016.day12.instructions.Jnz;
import info.jab.aoc2016.day12.instructions.LiteralSource;
import info.jab.aoc2016.day12.instructions.RegisterSource;
import info.jab.aoc2016.day12.instructions.Source;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembunny interpreter for Day 12
 * Supports instructions: cpy, inc, dec, jnz
 * Uses sealed class hierarchy following Effective Java principles (prefer class hierarchies to tagged classes)
 */
public class AssembunnyInterpreter implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Instruction> instructions = parseInstructions(lines);
        int[] registers = new int[4];

        execute(instructions, registers);
        return registers[0];
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Instruction> instructions = parseInstructions(lines);
        int[] registers = new int[4];
        registers[2] = 1; // c = 1

        execute(instructions, registers);
        return registers[0];
    }

    private List<Instruction> parseInstructions(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>(lines.size());
        for (String line : lines) {
            String[] parts = line.split(" ");
            String opStr = parts[0];

            switch (opStr) {
                case "cpy" -> {
                    // cpy x y
                    Source source = parseSource(parts[1]);
                    int targetRegister = getRegisterIndex(parts[2]);
                    instructions.add(new Cpy(source, targetRegister));
                }
                case "inc" -> {
                    // inc x
                    int register = getRegisterIndex(parts[1]);
                    instructions.add(new Inc(register));
                }
                case "dec" -> {
                    // dec x
                    int register = getRegisterIndex(parts[1]);
                    instructions.add(new Dec(register));
                }
                case "jnz" -> {
                    // jnz x y
                    Source source = parseSource(parts[1]);
                    int offset = Integer.parseInt(parts[2]);
                    instructions.add(new Jnz(source, offset));
                }
            }
        }
        return instructions;
    }

    private Source parseSource(String s) {
        if (isRegister(s)) {
            return new RegisterSource(getRegisterIndex(s));
        } else {
            return new LiteralSource(Integer.parseInt(s));
        }
    }

    private boolean isRegister(String s) {
        return s.length() == 1 && s.charAt(0) >= 'a' && s.charAt(0) <= 'd';
    }

    private int getRegisterIndex(String s) {
        return s.charAt(0) - 'a';
    }

    private void execute(List<Instruction> instructions, int[] registers) {
        int pc = 0;
        int size = instructions.size();

        while (pc < size) {
            Instruction ins = instructions.get(pc);
            pc = ins.execute(registers, pc);
        }
    }
}
