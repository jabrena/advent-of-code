package info.jab.aoc2024.day17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.putoet.resources.ResourceLines;

public class ChronospatialComputer2 {

    private record InputData(Integer a, Integer b, Integer c, List<Integer> opcodes) {}

    public InputData loadData(String fileName) {
        var list = ResourceLines.list(fileName);
        List<String> lines = list.stream().map(String::trim).filter(line -> !line.isEmpty()).toList();

        // Parse registers
        var a = Integer.parseInt(lines.get(0).split(": ")[1]);
        var b = Integer.parseInt(lines.get(1).split(": ")[1]);
        var c = Integer.parseInt(lines.get(2).split(": ")[1]);

        // Parse program
        String programLine = lines.get(3).split(": ")[1];
        var opcodes = Arrays.stream(programLine.split(",")).map(Integer::parseInt).toList();

        return new InputData(a, b, c, opcodes);
    }

    public String part1(String fileName) {
        var loadData = loadData(fileName);
        Computer computer = new Computer(loadData.a(), loadData.b(), loadData.c(), loadData.opcodes());
        var result = computer.execute();
        return computer.print(result);
    }

    public String part2(String fileName) {
        var loadData = loadData(fileName);
        List<Integer> remainingProgram = new ArrayList<>(loadData.opcodes());
        List<Integer> program = new ArrayList<>();
        long accumulator = 0L;
        while (!remainingProgram.isEmpty()) {
            --accumulator;
            program.addFirst(remainingProgram.removeLast());
            String pString = program.stream().map(Long::toString).collect(Collectors.joining(","));
            Computer computer;
            List<Integer> result2;
            do {
                ++accumulator;
                computer = new Computer(accumulator, loadData.b(), loadData.c(), loadData.opcodes());
                result2 = computer.execute();
            } while (!computer.print(result2).equals(pString));
            if (!remainingProgram.isEmpty()) {
                accumulator = accumulator << 3;
            }
        }
        return Long.toString(accumulator);
    }

    //Second version, more easier to read

    public String part22(String fileName) {
        var loadData = loadData(fileName);
        List<Integer> remainingProgram = new ArrayList<>(loadData.opcodes());
        List<Integer> program = new ArrayList<>();
        long accumulator = 0L;

        while (!remainingProgram.isEmpty()) {
            // Update the accumulator and move opcode from remainingProgram to program
            accumulator = processRemainingProgram(remainingProgram, program, accumulator);

            // Verify program execution until the output matches
            accumulator = findMatchingOutput(program, loadData, accumulator);

            // Adjust accumulator if there are more instructions to process
            if (!remainingProgram.isEmpty()) {
                accumulator = adjustAccumulator(accumulator);
            }
        }
        return Long.toString(accumulator);
    }

    private long processRemainingProgram(List<Integer> remainingProgram, List<Integer> program, long accumulator) {
        accumulator--; // Decrement accumulator
        program.add(0, remainingProgram.remove(remainingProgram.size() - 1)); // Add last element of remainingProgram to the start of program
        return accumulator;
    }

    private long findMatchingOutput(List<Integer> program, InputData loadData, long accumulator) {
        String programString = program.stream().map(Long::toString).collect(Collectors.joining(","));

        Computer computer;
        List<Integer> result;
        do {
            accumulator++;
            computer = new Computer(accumulator, loadData.b(), loadData.c(), loadData.opcodes());
            result = computer.execute();
        } while (!computer.print(result).equals(programString));

        return accumulator;
    }

    private long adjustAccumulator(long accumulator) {
        return accumulator << 3; // Equivalent to accumulator * 8
    }
}
