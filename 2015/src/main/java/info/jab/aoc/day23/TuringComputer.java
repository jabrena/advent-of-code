package info.jab.aoc.day23;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuringComputer implements Solver<Integer> {

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        return executeProgram(instructions, 0, 0);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructions = ResourceLines.list(fileName);
        return executeProgram(instructions, 1, 0);
    }

    private Integer executeProgram(List<String> instructions, int initialA, int initialB) {
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", initialA);
        registers.put("b", initialB);
        
        int pc = 0; // program counter
        
        while (pc >= 0 && pc < instructions.size()) {
            String instruction = instructions.get(pc);
            String[] parts = instruction.split("[, ]+");
            String cmd = parts[0];
            
            switch (cmd) {
                case "hlf" -> {
                    String register = parts[1];
                    registers.put(register, registers.get(register) / 2);
                    pc++;
                }
                case "tpl" -> {
                    String register = parts[1];
                    registers.put(register, registers.get(register) * 3);
                    pc++;
                }
                case "inc" -> {
                    String register = parts[1];
                    registers.put(register, registers.get(register) + 1);
                    pc++;
                }
                case "jmp" -> {
                    int offset = Integer.parseInt(parts[1]);
                    pc += offset;
                }
                case "jie" -> {
                    String register = parts[1];
                    int offset = Integer.parseInt(parts[2]);
                    if (registers.get(register) % 2 == 0) {
                        pc += offset;
                    } else {
                        pc++;
                    }
                }
                case "jio" -> {
                    String register = parts[1];
                    int offset = Integer.parseInt(parts[2]);
                    if (registers.get(register) == 1) {
                        pc += offset;
                    } else {
                        pc++;
                    }
                }
                default -> throw new IllegalArgumentException("Unknown instruction: " + cmd);
            }
        }
        
        return registers.get("b");
    }
}