package info.jab.aoc2016.day12;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembunny interpreter for Day 12
 * Supports instructions: cpy, inc, dec, jnz
 */
public class AssembunnyInterpreter implements Solver<Integer> {

    private enum OpCode { CPY, INC, DEC, JNZ }

    private record Instruction(OpCode op, int arg1, int arg2, boolean arg1IsReg) {}

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
                    int arg1;
                    boolean arg1IsReg = false;
                    if (isRegister(parts[1])) {
                        arg1 = getRegisterIndex(parts[1]);
                        arg1IsReg = true;
                    } else {
                        arg1 = Integer.parseInt(parts[1]);
                    }
                    int arg2 = getRegisterIndex(parts[2]);
                    instructions.add(new Instruction(OpCode.CPY, arg1, arg2, arg1IsReg));
                }
                case "inc" -> {
                    // inc x
                    int arg1 = getRegisterIndex(parts[1]);
                    instructions.add(new Instruction(OpCode.INC, arg1, 0, true));
                }
                case "dec" -> {
                    // dec x
                    int arg1 = getRegisterIndex(parts[1]);
                    instructions.add(new Instruction(OpCode.DEC, arg1, 0, true));
                }
                case "jnz" -> {
                    // jnz x y
                    int arg1;
                    boolean arg1IsReg = false;
                    if (isRegister(parts[1])) {
                        arg1 = getRegisterIndex(parts[1]);
                        arg1IsReg = true;
                    } else {
                        arg1 = Integer.parseInt(parts[1]);
                    }
                    int arg2 = Integer.parseInt(parts[2]);
                    instructions.add(new Instruction(OpCode.JNZ, arg1, arg2, arg1IsReg));
                }
            }
        }
        return instructions;
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
            
            switch (ins.op) {
                case CPY -> {
                    int val = ins.arg1IsReg ? registers[ins.arg1] : ins.arg1;
                    registers[ins.arg2] = val;
                    pc++;
                }
                case INC -> {
                    registers[ins.arg1]++;
                    pc++;
                }
                case DEC -> {
                    registers[ins.arg1]--;
                    pc++;
                }
                case JNZ -> {
                    int val = ins.arg1IsReg ? registers[ins.arg1] : ins.arg1;
                    if (val != 0) {
                        pc += ins.arg2;
                    } else {
                        pc++;
                    }
                }
            }
        }
    }
}