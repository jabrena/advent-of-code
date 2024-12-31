package info.jab.aoc.day7;

import java.util.*;
import java.util.regex.*;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class Circuit implements Solver<Integer> {

    private Map<String, Wire> wires;
    private List<Instruction> instructions;
    private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("(\\w+|\\d+)\\s*(AND|OR|LSHIFT|RSHIFT|->|NOT)?\\s*(\\w+|\\d+)?\\s*->\\s*(\\w+)");

    public Circuit() {
        this.wires = new HashMap<>();
        this.instructions = new ArrayList<>();
    }

    public void addInstruction(String instruction) {
        Matcher matcher = INSTRUCTION_PATTERN.matcher(instruction);
        if (matcher.find()) {
            instructions.add(new Instruction(instruction));
        }
    }

    public void processInstructions() {
        while (!instructions.isEmpty()) {
            Iterator<Instruction> iterator = instructions.iterator();
            while (iterator.hasNext()) {
                Instruction instruction = iterator.next();
                if (tryProcessInstruction(instruction)) {
                    iterator.remove();
                }
            }
        }
    }

    private boolean tryProcessInstruction(Instruction instruction) {
        String[] parts = instruction.parse();
        String operation = parts[1];
        String output = parts[3];

        try {
            if (operation == null || operation.equals("->")) {
                int value = getValue(parts[0]);
                setWireValue(output, value);
                return true;
            } else if (operation.equals("NOT")) {
                int value = getValue(parts[0]);
                setWireValue(output, ~value & 0xFFFF);
                return true;
            } else {
                int left = getValue(parts[0]);
                int right = getValue(parts[2]);
                
                int result = switch(operation) {
                    case "AND" -> left & right;
                    case "OR" -> left | right;
                    case "LSHIFT" -> (left << right) & 0xFFFF;
                    case "RSHIFT" -> (left >>> right) & 0xFFFF;
                    default -> throw new IllegalArgumentException("Unknown operation: " + operation);
                };
                
                setWireValue(output, result);
                return true;
            }
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private int getValue(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Wire wire = wires.get(input);
            if (wire != null && wire.hasSignal()) {
                return wire.getSignal();
            }
            throw new NoSuchElementException("Wire not found or no signal: " + input);
        }
    }

    private void setWireValue(String wireName, int value) {
        Wire wire = wires.computeIfAbsent(wireName, k -> new Wire(wireName));
        wire.setSignal(value);
    }

    public Integer getWireSignal(String wireName) {
        Wire wire = wires.get(wireName);
        return wire != null && wire.hasSignal() ? wire.getSignal() : null;
    }


    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        lines.stream().forEach(this::addInstruction);

        processInstructions();
        return getWireSignal("a");
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'solvePartTwo'");
    }
}
