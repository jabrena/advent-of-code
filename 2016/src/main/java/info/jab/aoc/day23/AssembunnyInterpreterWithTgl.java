package info.jab.aoc.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembunny interpreter for Day 23
 * Supports instructions: cpy, inc, dec, jnz, tgl
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
        
        executeOptimized(instructions, registers);
        return registers.get("a");
    }
    
    private void executeOptimized(List<String> instructions, Map<String, Integer> registers) {
        int pc = 0;
        int loopCount = 0;
        int lastPc0Check = -1;
        
        while (pc >= 0 && pc < instructions.size()) {
            // Optimize multiplication loop pattern whenever we're at the start (pc=0)
            // Pattern: cpy a b, dec b, cpy a d, cpy 0 a, cpy b c, inc a, dec c, jnz c -2, dec d, jnz d -5
            // This pattern computes: a = a * (a - 1)
            // Check every time we're at pc=0 (after loops back)
            if (pc == 0 && instructions.size() >= 10) {
                String[] insts = new String[10];
                for (int i = 0; i < 10 && pc + i < instructions.size(); i++) {
                    insts[i] = instructions.get(pc + i);
                }
                
                // Check for multiplication pattern (even if instructions were modified by tgl)
                // We check the instruction types, not exact strings
                if (insts[0] != null && insts[0].startsWith("cpy a ") &&
                    insts[1] != null && insts[1].equals("dec b") &&
                    insts[2] != null && insts[2].startsWith("cpy a ") &&
                    insts[3] != null && insts[3].equals("cpy 0 a") &&
                    insts[4] != null && insts[4].equals("cpy b c") &&
                    insts[5] != null && insts[5].equals("inc a") &&
                    insts[6] != null && insts[6].equals("dec c") &&
                    insts[7] != null && insts[7].equals("jnz c -2") &&
                    insts[8] != null && insts[8].equals("dec d") &&
                    insts[9] != null && insts[9].equals("jnz d -5")) {
                    
                    int a = registers.getOrDefault("a", 0);
                    if (a > 1) {
                        // Optimize: a = a * (a - 1)
                        registers.put("a", a * (a - 1));
                        registers.put("b", a - 2);
                        registers.put("c", 0);
                        registers.put("d", 0);
                        pc = 10; // Skip the multiplication loop
                        continue;
                    }
                }
            }
            
            // Continue with normal execution
            int oldPc = pc;
            pc = executeStep(instructions, registers, pc);
            
            // Track if we're looping back to start
            if (pc == 0 && oldPc > 0) {
                loopCount++;
            }
        }
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
