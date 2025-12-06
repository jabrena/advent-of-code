package info.jab.aoc2015.day23.instructions;

import java.util.Map;

record Tpl(String register) implements Instruction {
    @Override
    public int execute(Map<String, Integer> registers, int pc) {
        registers.put(register, registers.get(register) * 3);
        return pc + 1;
    }
}

