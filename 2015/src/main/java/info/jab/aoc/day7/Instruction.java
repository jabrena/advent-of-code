package info.jab.aoc.day7;

// Instruction.java
public class Instruction {
    private final String raw;

    public Instruction(String instruction) {
        this.raw = instruction;
    }

    public String[] parse() {
        String[] parts = new String[4]; // [input1, operation, input2, output]
        
        String[] splitArrow = raw.split("->");
        String output = splitArrow[1].trim();
        String input = splitArrow[0].trim();
        
        if (input.startsWith("NOT")) {
            parts[0] = input.substring(4).trim();
            parts[1] = "NOT";
        } else if (input.contains("AND")) {
            String[] ops = input.split("AND");
            parts[0] = ops[0].trim();
            parts[1] = "AND";
            parts[2] = ops[1].trim();
        } else if (input.contains("OR")) {
            String[] ops = input.split("OR");
            parts[0] = ops[0].trim();
            parts[1] = "OR";
            parts[2] = ops[1].trim();
        } else if (input.contains("LSHIFT")) {
            String[] ops = input.split("LSHIFT");
            parts[0] = ops[0].trim();
            parts[1] = "LSHIFT";
            parts[2] = ops[1].trim();
        } else if (input.contains("RSHIFT")) {
            String[] ops = input.split("RSHIFT");
            parts[0] = ops[0].trim();
            parts[1] = "RSHIFT";
            parts[2] = ops[1].trim();
        } else {
            parts[0] = input;
            parts[1] = "->";
        }
        
        parts[3] = output;
        return parts;
    }
}
