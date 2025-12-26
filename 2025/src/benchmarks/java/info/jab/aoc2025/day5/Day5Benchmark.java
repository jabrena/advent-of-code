package info.jab.aoc2025.day5;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day5Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Range range = new Range();
        Range2 range2 = new Range2();
        Range3 range3 = new Range3();
        String fileName = "/day5/day5-input.txt";
    }

    @Benchmark
    public void range_part1(St st) {
        st.range.solvePartOne(st.fileName);
    }

    @Benchmark
    public void range2_part1(St st) {
        st.range2.solvePartOne(st.fileName);
    }

    @Benchmark
    public void range_part2(St st) {
        st.range.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void range2_part2(St st) {
        st.range2.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void range3_part1(St st) {
        st.range3.solvePartOne(st.fileName);
    }

    @Benchmark
    public void range3_part2(St st) {
        st.range3.solvePartTwo(st.fileName);
    }
}

