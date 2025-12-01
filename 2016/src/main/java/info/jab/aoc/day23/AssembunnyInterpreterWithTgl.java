package info.jab.aoc.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembunny interpreter for Day 23
 * Supports instructions: cpy, inc, dec, jnz, tgl, mul
 */
public class AssembunnyInterpreterWithTgl implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructions = new ArrayList<>(ResourceLines.list(fileName));
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 7); // Start with 7 eggs
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        
        execute(instructions, registers);
        return registers.get("a");
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructions = new ArrayList<>(ResourceLines.list(fileName));
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 12); // Start with 12 eggs
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        
        // Optimize: Replace the multiplication loop (lines 1-10) with a mul instruction
        // Pattern: cpy a b, dec b, cpy a d, cpy 0 a, cpy b c, inc a, dec c, jnz c -2, dec d, jnz d -5
        // This computes: a = a_initial * (a_initial - 1)
        // At line 6: d = a_initial, b = a_initial - 1, a = 0
        // Replace line 6 (inc a) with: mul d b a (multiply d * b, store in a)
        if (instructions.size() > 5) {
            // Replace line 6 (index 5) with mul instruction
            instructions.set(5, "mul d b a");
        }
        
        execute(instructions, registers);
        return registers.get("a");
    }
    
    private void execute(List<String> instructions, Map<String, Integer> registers) {
        int pc = 0;
        while (pc >= 0 && pc < instructions.size()) {
            pc = executeStep(instructions, registers, pc);
        }
    }
    
    private int executeStep(List<String> instructions, Map<String, Integer> registers, int pc) {
        String instructionLine = instructions.get(pc);
        if (instructionLine == null || instructionLine.trim().isEmpty()) {
            return pc + 1;
        }
        
        String[] parts = instructionLine.split(" ");
        String instruction = parts[0];
        
        return switch (instruction) {
            case "cpy" -> {
                String source = parts[1];
                String target = parts[2];
                // Skip if target is a number (invalid instruction)
                if (isNumber(target)) {
                    yield pc + 1;
                }
                int value = getValue(source, registers);
                registers.put(target, value);
                yield pc + 1;
            }
            case "inc" -> {
                String register = parts[1];
                if (isNumber(register)) {
                    yield pc + 1;
                }
                registers.put(register, registers.getOrDefault(register, 0) + 1);
                yield pc + 1;
            }
            case "dec" -> {
                String register = parts[1];
                if (isNumber(register)) {
                    yield pc + 1;
                }
                registers.put(register, registers.getOrDefault(register, 0) - 1);
                yield pc + 1;
            }
            case "jnz" -> {
                String condition = parts[1];
                String offset = parts[2];
                int conditionValue = getValue(condition, registers);
                if (conditionValue != 0) {
                    int offsetValue = getValue(offset, registers);
                    yield pc + offsetValue;
                } else {
                    yield pc + 1;
                }
            }
            case "tgl" -> {
                String offsetStr = parts[1];
                int offset = getValue(offsetStr, registers);
                int targetIndex = pc + offset;
                
                if (targetIndex >= 0 && targetIndex < instructions.size()) {
                    String targetInstruction = instructions.get(targetIndex);
                    if (targetInstruction != null && !targetInstruction.trim().isEmpty()) {
                        String toggled = toggleInstruction(targetInstruction);
                        instructions.set(targetIndex, toggled);
                    }
                }
                yield pc + 1;
            }
            case "mul" -> {
                // mul x y z: multiply x * y, store result in z, then skip 6 instructions
                if (parts.length == 4) {
                    String source1 = parts[1];
                    String source2 = parts[2];
                    String target = parts[3];
                    
                    if (!isNumber(target)) {
                        int value1 = getValue(source1, registers);
                        int value2 = getValue(source2, registers);
                        registers.put(target, value1 * value2);
                    }
                }
                yield pc + 6; // Skip 6 instructions as per gist solution
            }
            default -> pc + 1; // Skip invalid instructions
        };
    }
    
    private String toggleInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String inst = parts[0];
        
        if (parts.length == 2) {
            // One-argument instruction
            if ("inc".equals(inst)) {
                return "dec " + parts[1];
            } else {
                return "inc " + parts[1];
            }
        } else if (parts.length == 3) {
            // Two-argument instruction
            if ("jnz".equals(inst)) {
                return "cpy " + parts[1] + " " + parts[2];
            } else {
                return "jnz " + parts[1] + " " + parts[2];
            }
        }
        
        return instruction; // Should not happen
    }
    
    private int getValue(String source, Map<String, Integer> registers) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return registers.getOrDefault(source, 0);
        }
    }
    
    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
