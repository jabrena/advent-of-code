package info.jab.aoc2015.day23.instructions;

import java.util.Map;

record Inc(String register) implements Instruction {
    @Override
    public int execute(Map<String, Integer> registers, int pc) {
        registers.put(register, registers.get(register) + 1);
        return pc + 1;
    }
}

