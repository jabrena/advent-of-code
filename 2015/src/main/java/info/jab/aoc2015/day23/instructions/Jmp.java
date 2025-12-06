package info.jab.aoc2015.day23.instructions;

import java.util.Map;

record Jmp(int offset) implements Instruction {
    @Override
    public int execute(Map<String, Integer> registers, int pc) {
        return pc + offset;
    }
}

