package info.jab.aoc2017.day8;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;
import java.util.HashMap;
import java.util.Map;

public class Day8 implements Day<Integer> {

    private record Instruction(
        String register,
        String operation,
        int amount,
        String conditionRegister,
        String conditionOperator,
        int conditionValue
    ) {}

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        var registers = new HashMap<String, Integer>();
        
        for (var line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            var instruction = parseInstruction(line);
            if (evaluateCondition(registers, instruction)) {
                executeInstruction(registers, instruction);
            }
        }
        
        return registers.values().stream()
            .mapToInt(Integer::intValue)
            .max()
            .orElse(0);
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        var registers = new HashMap<String, Integer>();
        var maxValueEver = Integer.MIN_VALUE;
        
        for (var line : lines) {
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            var instruction = parseInstruction(line);
            if (evaluateCondition(registers, instruction)) {
                executeInstruction(registers, instruction);
                // Track the maximum value ever held
                var currentMax = registers.values().stream()
                    .mapToInt(Integer::intValue)
                    .max()
                    .orElse(Integer.MIN_VALUE);
                if (currentMax > maxValueEver) {
                    maxValueEver = currentMax;
                }
            }
        }
        
        return maxValueEver;
    }

    private Instruction parseInstruction(String line) {
        // Format: "b inc 5 if a > 1"
        var parts = line.trim().split("\\s+");
        if (parts.length < 7) {
            throw new IllegalArgumentException("Invalid instruction format: " + line);
        }
        var register = parts[0];
        var operation = parts[1]; // "inc" or "dec"
        var amount = Integer.parseInt(parts[2]);
        var conditionRegister = parts[4];
        var conditionOperator = parts[5]; // ">", "<", ">=", "<=", "==", "!="
        var conditionValue = Integer.parseInt(parts[6]);
        
        return new Instruction(register, operation, amount, conditionRegister, conditionOperator, conditionValue);
    }

    private boolean evaluateCondition(Map<String, Integer> registers, Instruction instruction) {
        var registerValue = registers.getOrDefault(instruction.conditionRegister(), 0);
        var conditionValue = instruction.conditionValue();
        
        return switch (instruction.conditionOperator()) {
            case ">" -> registerValue > conditionValue;
            case "<" -> registerValue < conditionValue;
            case ">=" -> registerValue >= conditionValue;
            case "<=" -> registerValue <= conditionValue;
            case "==" -> registerValue == conditionValue;
            case "!=" -> registerValue != conditionValue;
            default -> false;
        };
    }

    private void executeInstruction(Map<String, Integer> registers, Instruction instruction) {
        var currentValue = registers.getOrDefault(instruction.register(), 0);
        var amount = instruction.amount();
        
        if ("inc".equals(instruction.operation())) {
            registers.put(instruction.register(), currentValue + amount);
        } else if ("dec".equals(instruction.operation())) {
            registers.put(instruction.register(), currentValue - amount);
        }
    }
}
