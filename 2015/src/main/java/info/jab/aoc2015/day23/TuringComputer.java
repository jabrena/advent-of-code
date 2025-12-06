package info.jab.aoc2015.day23;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import info.jab.aoc2015.day23.instructions.Instruction;
import info.jab.aoc2015.day23.instructions.InstructionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuringComputer implements Solver<Integer> {

    /**
     * Maximum number of instructions to execute to prevent DoS attacks.
     * This limit prevents infinite loops in program execution.
     */
    private static final int MAX_INSTRUCTIONS = 1_000_000;

    @Override
    public Integer solvePartOne(String fileName) {
        List<String> instructionLines = ResourceLines.list(fileName);
        return executeProgram(instructionLines, 0, 0);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        List<String> instructionLines = ResourceLines.list(fileName);
        return executeProgram(instructionLines, 1, 0);
    }

    private Integer executeProgram(List<String> instructionLines, int initialA, int initialB) {
        List<Instruction> instructions = InstructionParser.parse(instructionLines);
        Map<String, Integer> registers = new HashMap<>();
        registers.put("a", initialA);
        registers.put("b", initialB);

        int pc = 0;
        int instructionCount = 0;

        while (pc >= 0 && pc < instructions.size()) {
            if (instructionCount >= MAX_INSTRUCTIONS) {
                throw new IllegalStateException(
                        "Program execution exceeded maximum instruction limit of " + MAX_INSTRUCTIONS);
            }
            
            Instruction instruction = instructions.get(pc);
            pc = instruction.execute(registers, pc);
            instructionCount++;
        }

        return registers.get("b");
    }
}
