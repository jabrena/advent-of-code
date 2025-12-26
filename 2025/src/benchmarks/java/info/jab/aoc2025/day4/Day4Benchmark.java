package info.jab.aoc2025.day4;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day4Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        GridNeighbor gridNeighbor = new GridNeighbor();
        GridNeighbor2 gridNeighbor2 = new GridNeighbor2();
        String fileName = "/day4/day4-input.txt";
    }

    @Benchmark
    public void gridNeighbor_part1(St st) {
        st.gridNeighbor.solvePartOne(st.fileName);
    }

    @Benchmark
    public void gridNeighbor2_part1(St st) {
        st.gridNeighbor2.solvePartOne(st.fileName);
    }

    @Benchmark
    public void gridNeighbor_part2(St st) {
        st.gridNeighbor.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void gridNeighbor2_part2(St st) {
        st.gridNeighbor2.solvePartTwo(st.fileName);
    }
}

