package info.jab.aoc2025.day10;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day10Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        ButtonPressOptimizer buttonPressOptimizer = new ButtonPressOptimizer();
        String fileName = "/day10/day10-input.txt";
    }

    @Benchmark
    public void buttonPressOptimizer_part1(St st) {
        st.buttonPressOptimizer.solvePartOne(st.fileName);
    }

    @Benchmark
    public void buttonPressOptimizer_part2(St st) {
        st.buttonPressOptimizer.solvePartTwo(st.fileName);
    }
}

