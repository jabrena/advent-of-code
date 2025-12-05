package info.jab.aoc2015.day7;

public record Instruction(String input1, String operation, String input2, String output) {
    
    private static final String LSHIFT_OPERATION = "LSHIFT";
    private static final String RSHIFT_OPERATION = "RSHIFT";
    private static final String ARROW_SEPARATOR = "->";

    public static Instruction parse(String raw) {
        if (raw == null || !raw.contains(ARROW_SEPARATOR)) {
            throw new IllegalArgumentException("Invalid instruction: " + raw);
        }
        
        String[] splitArrow = raw.split(ARROW_SEPARATOR);
        if (splitArrow.length != 2) {
            throw new IllegalArgumentException("Invalid instruction: " + raw);
        }
        
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
        } else if (input.contains(LSHIFT_OPERATION)) {
            String[] ops = input.split(LSHIFT_OPERATION);
            input1 = ops[0].trim();
            operation = LSHIFT_OPERATION;
            input2 = ops[1].trim();
        } else if (input.contains(RSHIFT_OPERATION)) {
            String[] ops = input.split(RSHIFT_OPERATION);
            input1 = ops[0].trim();
            operation = RSHIFT_OPERATION;
            input2 = ops[1].trim();
        } else {
            input1 = input;
            operation = "->";
        }
        
        return new Instruction(input1, operation, input2, output);
    }
}
