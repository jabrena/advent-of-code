package info.jab.aoc2015.day23.instructions;

import java.util.Map;

record Jio(String register, int offset) implements Instruction {
    @Override
    public int execute(Map<String, Integer> registers, int pc) {
        return registers.get(register) == 1 ? pc + offset : pc + 1;
    }
}

