package info.jab.aoc2015.day23.instructions;

import java.util.List;

public class InstructionParser {
    
    private InstructionParser() {
        // Utility class - prevent instantiation
    }
    public static List<Instruction> parse(List<String> lines) {
        return lines.stream()
            .map(InstructionParser::parseInstruction)
            .toList();
    }

    private static Instruction parseInstruction(String line) {
        String[] parts = line.split("[, ]+");
        String cmd = parts[0];
        
        return switch (cmd) {
            case "hlf" -> new Hlf(parts[1]);
            case "tpl" -> new Tpl(parts[1]);
            case "inc" -> new Inc(parts[1]);
            case "jmp" -> new Jmp(Integer.parseInt(parts[1]));
            case "jie" -> new Jie(parts[1], Integer.parseInt(parts[2]));
            case "jio" -> new Jio(parts[1], Integer.parseInt(parts[2]));
            default -> throw new IllegalArgumentException("Unknown instruction: " + cmd);
        };
    }
}

