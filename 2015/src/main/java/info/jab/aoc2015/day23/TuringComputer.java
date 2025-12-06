package info.jab.aoc2015.day23;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import info.jab.aoc2015.day23.instructions.Instruction;
import info.jab.aoc2015.day23.instructions.InstructionParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TuringComputer implements Solver<Integer> {

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

        while (pc >= 0 && pc < instructions.size()) {
            Instruction instruction = instructions.get(pc);
            pc = instruction.execute(registers, pc);
        }

        return registers.get("b");
    }
}
