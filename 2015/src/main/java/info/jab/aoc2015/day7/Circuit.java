package info.jab.aoc2015.day7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

public class Circuit implements Solver<Integer> {

    private Map<String, Wire> wires;
 
    public Circuit() {
        this.wires = new HashMap<>();
    }

    private int getValue(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException _) {
            Wire wire = wires.get(input);
            if (wire != null && wire.hasSignal()) {
                return wire.getSignal();
            }
            throw new IllegalStateException("Wire not found or no signal: " + input);
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

    private boolean isReady(Instruction instruction) {
        try {
            if (instruction.operation() == null || instruction.operation().equals("->") 
                    || instruction.operation().equals("NOT")) {
                getValue(instruction.input1());
                return true;
            } else {
                getValue(instruction.input1());
                getValue(instruction.input2());
                return true;
            }
        } catch (IllegalStateException _) {
            return false;
        }
    }

    private void processInstruction(Instruction instruction) {
        if (instruction.operation() == null || instruction.operation().equals("->")) {
            int value = getValue(instruction.input1());
            setWireValue(instruction.output(), value);
        } else if (instruction.operation().equals("NOT")) {
            int value = getValue(instruction.input1());
            setWireValue(instruction.output(), ~value & 0xFFFF);
        } else {
            int left = getValue(instruction.input1());
            int right = getValue(instruction.input2());
            
            int result = switch(instruction.operation()) {
                case "AND" -> left & right;
                case "OR" -> left | right;
                case "LSHIFT" -> (left << right) & 0xFFFF;
                case "RSHIFT" -> (left >>> right) & 0xFFFF;
                default -> throw new IllegalArgumentException("Unknown operation: " + instruction.operation());
            };
            
            setWireValue(instruction.output(), result);
        }
    }

    private void processInstructionsTopologically(List<Instruction> instructions) {
        // Build dependency graph: wire -> list of instructions that depend on it
        Map<String, List<Instruction>> wireDependencies = new HashMap<>();
        Set<Instruction> remaining = new HashSet<>(instructions);
        
        for (Instruction inst : instructions) {
            if (inst.input1() != null && !isNumeric(inst.input1())) {
                wireDependencies.computeIfAbsent(inst.input1(), k -> new ArrayList<>()).add(inst);
            }
            if (inst.input2() != null && !isNumeric(inst.input2())) {
                wireDependencies.computeIfAbsent(inst.input2(), k -> new ArrayList<>()).add(inst);
            }
        }
        
        // Process instructions in topological order using Kahn's algorithm
        Queue<Instruction> readyQueue = new LinkedList<>();
        
        // Initially, add instructions that are ready (all inputs are numeric or already set)
        for (Instruction inst : instructions) {
            if (isReady(inst)) {
                readyQueue.offer(inst);
            }
        }
        
        while (!readyQueue.isEmpty()) {
            Instruction current = readyQueue.poll();
            if (!remaining.contains(current)) {
                continue;
            }
            
            remaining.remove(current);
            processInstruction(current);
            
            // Check if any instructions waiting for this wire's output are now ready
            String outputWire = current.output();
            List<Instruction> dependent = wireDependencies.get(outputWire);
            if (dependent != null) {
                for (Instruction dep : dependent) {
                    if (remaining.contains(dep) && isReady(dep)) {
                        readyQueue.offer(dep);
                    }
                }
            }
        }
        
        // Process any remaining instructions that might be ready now
        while (!remaining.isEmpty()) {
            boolean progress = false;
            List<Instruction> toProcess = new ArrayList<>();
            
            for (Instruction inst : remaining) {
                if (isReady(inst)) {
                    toProcess.add(inst);
                    progress = true;
                }
            }
            
            if (!progress) {
                throw new IllegalStateException("Circular dependency detected or missing inputs");
            }
            
            for (Instruction inst : toProcess) {
                remaining.remove(inst);
                processInstruction(inst);
                
                String outputWire = inst.output();
                List<Instruction> dependent = wireDependencies.get(outputWire);
                if (dependent != null) {
                    for (Instruction dep : dependent) {
                        if (remaining.contains(dep) && isReady(dep)) {
                            readyQueue.offer(dep);
                        }
                    }
                }
            }
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException _) {
            return false;
        }
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Instruction> instructions = lines.stream()
            .map(Instruction::parse)
            .toList();

        // Use topological sort for O(G) complexity
        processInstructionsTopologically(instructions);
        return getWireSignal("a");
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        // Solve part 1 first
        Integer signalA = solvePartOne(fileName);
        
        // Reset the circuit
        this.wires = new HashMap<>();
        
        // Solve again with 'a' value as input for 'b'
        var lines = ResourceLines.list(fileName);
        List<Instruction> instructions = lines.stream()
            .map(Instruction::parse)
            .toList();
            
        // Set 'b' wire to previous 'a' result
        setWireValue("b", signalA);
        
        // Filter out instruction that sets 'b'
        List<Instruction> filteredInstructions = instructions.stream()
            .filter(inst -> !inst.output().equals("b"))
            .toList();
        
        // Use topological sort for O(G) complexity
        processInstructionsTopologically(filteredInstructions);
        
        return getWireSignal("a");
    }
}
