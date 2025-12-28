package info.jab.aoc2025.day11;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day11Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        GraphPathCounter graphPathCounter = new GraphPathCounter();
        String fileName = "/day11/day11-input.txt";
    }

    @Benchmark
    public void graphPathCounter_part1(St st) {
        st.graphPathCounter.solvePartOne(st.fileName);
    }

    @Benchmark
    public void graphPathCounter_part2(St st) {
        st.graphPathCounter.solvePartTwo(st.fileName);
    }
}

