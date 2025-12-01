package info.jab.aoc2016.day23;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Temporary analyzer to understand what the program computes
 */
public class ProgramAnalyzer {
    
    public static void main(String[] args) {
        // Test with small values to understand the pattern
        for (int a = 1; a <= 7; a++) {
            int result = compute(a);
            System.out.println("a=" + a + " -> result=" + result);
        }
    }
    
    private static int compute(int initialA) {
        List<String> instructions = new ArrayList<>(ResourceLines.list("/day23/day23-input.txt"));
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", initialA);
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        
        execute(instructions, registers);
        return registers.get("a");
    }
    
    private static void execute(List<String> instructions, Map<String, Integer> registers) {
        int pc = 0;
        long steps = 0;
        
        while (pc >= 0 && pc < instructions.size()) {
            steps++;
            if (steps % 1000000 == 0) {
                System.out.println("Steps: " + steps + ", pc=" + pc + ", a=" + registers.get("a"));
            }
            
            String instructionLine = instructions.get(pc);
            if (instructionLine == null || instructionLine.trim().isEmpty()) {
                pc++;
                continue;
            }
            
            String[] parts = instructionLine.split(" ");
            String instruction = parts[0];
            
            switch (instruction) {
                case "cpy" -> {
                    String source = parts[1];
                    String target = parts[2];
                    if (isNumber(target)) {
                        pc++;
                        break;
                    }
                    int value = getValue(source, registers);
                    registers.put(target, value);
                    pc++;
                }
                case "inc" -> {
                    String register = parts[1];
                    if (isNumber(register)) {
                        pc++;
                        break;
                    }
                    registers.put(register, registers.getOrDefault(register, 0) + 1);
                    pc++;
                }
                case "dec" -> {
                    String register = parts[1];
                    if (isNumber(register)) {
                        pc++;
                        break;
                    }
                    registers.put(register, registers.getOrDefault(register, 0) - 1);
                    pc++;
                }
                case "jnz" -> {
                    String condition = parts[1];
                    String offset = parts[2];
                    int conditionValue = getValue(condition, registers);
                    if (conditionValue != 0) {
                        int offsetValue = getValue(offset, registers);
                        pc += offsetValue;
                    } else {
                        pc++;
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
                    pc++;
                }
                default -> pc++;
            }
        }
        
        System.out.println("Total steps: " + steps);
    }
    
    private static String toggleInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String inst = parts[0];
        
        if (parts.length == 2) {
            if ("inc".equals(inst)) {
                return "dec " + parts[1];
            } else {
                return "inc " + parts[1];
            }
        } else if (parts.length == 3) {
            if ("jnz".equals(inst)) {
                return "cpy " + parts[1] + " " + parts[2];
            } else {
                return "jnz " + parts[1] + " " + parts[2];
            }
        }
        
        return instruction;
    }
    
    private static int getValue(String source, Map<String, Integer> registers) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return registers.getOrDefault(source, 0);
        }
    }
    
    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


