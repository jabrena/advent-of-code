package info.jab.aoc2015.day23.instructions;

import java.util.Map;

public sealed interface Instruction permits Hlf, Tpl, Inc, Jmp, Jie, Jio {
    int execute(Map<String, Integer> registers, int pc);
}

