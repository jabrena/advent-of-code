package info.jab.aoc2025.day9;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day9Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        MaxRectangleArea maxRectangleArea = new MaxRectangleArea();
        String fileName = "/day9/day9-input.txt";
    }

    @Benchmark
    public void maxRectangleArea_part1(St st) {
        st.maxRectangleArea.solvePartOne(st.fileName);
    }

    @Benchmark
    public void maxRectangleArea_part2(St st) {
        st.maxRectangleArea.solvePartTwo(st.fileName);
    }
}

