package info.jab.aoc.day12;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Assembunny interpreter for Day 12
 * Supports instructions: cpy, inc, dec, jnz
 */
public class AssembunnyInterpreter implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", 0);
        registers.put("d", 0);
        
        execute(instructions, registers);
        return registers.get("a");
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", 0);
        registers.put("b", 0);
        registers.put("c", 1); // For part 2, c starts at 1
        registers.put("d", 0);
        
        execute(instructions, registers);
        return registers.get("a");
    }
    
    private void execute(List<String> instructions, Map<String, Integer> registers) {
        int pc = 0; // program counter
        
        while (pc < instructions.size()) {
            String[] parts = instructions.get(pc).split(" ");
            String instruction = parts[0];
            
            switch (instruction) {
                case "cpy" -> {
                    String source = parts[1];
                    String target = parts[2];
                    int value = getValue(source, registers);
                    registers.put(target, value);
                    pc++;
                }
                case "inc" -> {
                    String register = parts[1];
                    registers.put(register, registers.get(register) + 1);
                    pc++;
                }
                case "dec" -> {
                    String register = parts[1];
                    registers.put(register, registers.get(register) - 1);
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
                default -> throw new IllegalArgumentException("Unknown instruction: " + instruction);
            }
        }
    }
    
    private int getValue(String source, Map<String, Integer> registers) {
        try {
            return Integer.parseInt(source);
        } catch (NumberFormatException e) {
            return registers.get(source);
        }
    }
}