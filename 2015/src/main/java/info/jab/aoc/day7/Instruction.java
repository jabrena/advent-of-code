package info.jab.aoc.day7;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record Instruction(String input1, String operation, String input2, String output) {
    
    private static final String INSTRUCTION_PATTERN = "^(?:(?:([a-z0-9]+)\\s+)?([A-Z]+)\\s+([a-z0-9]+)|([a-z0-9]+))\\s+->\\s+([a-z]+)$";
    private static final Pattern INSTRUCTION_PATTERN_COMPILED = Pattern.compile(INSTRUCTION_PATTERN);

    public static Instruction parse(String raw) {
        Matcher matcher = INSTRUCTION_PATTERN_COMPILED.matcher(raw);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid instruction: " + raw);
        }

        String[] splitArrow = raw.split("->");
        String output = splitArrow[1].trim();
        String input = splitArrow[0].trim();
        
        String input1 = null;
        String operation = null;
        String input2 = null;
        
        if (input.startsWith("NOT")) {
            input1 = input.substring(4).trim();
            operation = "NOT";
        } else if (input.contains("AND")) {
            String[] ops = input.split("AND");
            input1 = ops[0].trim();
            operation = "AND";
            input2 = ops[1].trim();
        } else if (input.contains("OR")) {
            String[] ops = input.split("OR");
            input1 = ops[0].trim();
            operation = "OR";
            input2 = ops[1].trim();
        } else if (input.contains("LSHIFT")) {
            String[] ops = input.split("LSHIFT");
            input1 = ops[0].trim();
            operation = "LSHIFT";
            input2 = ops[1].trim();
        } else if (input.contains("RSHIFT")) {
            String[] ops = input.split("RSHIFT");
            input1 = ops[0].trim();
            operation = "RSHIFT";
            input2 = ops[1].trim();
        } else {
            input1 = input;
            operation = "->";
        }
        
        return new Instruction(input1, operation, input2, output);
    }
}
