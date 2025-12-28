package info.jab.aoc2025.day12;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day12Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        ShapePacking shapePacking = new ShapePacking();
        String fileName = "/day12/day12-input.txt";
    }

    @Benchmark
    public void shapePacking_part1(St st) {
        st.shapePacking.solvePartOne(st.fileName);
    }

    @Benchmark
    public void shapePacking_part2(St st) {
        st.shapePacking.solvePartTwo(st.fileName);
    }
}

