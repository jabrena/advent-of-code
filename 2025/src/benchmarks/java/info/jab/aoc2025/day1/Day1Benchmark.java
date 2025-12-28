package info.jab.aoc2025.day1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day1Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        DialRotator dialRotator = new DialRotator();
        DialRotator2 dialRotator2 = new DialRotator2();
        DialRotator3 dialRotator3 = new DialRotator3();
        String fileName = "/day1/day1-input.txt";
    }

    // Part 1 benchmarks - grouped for easier comparison
    @Benchmark
    public void dialRotator_part1(St st) {
        st.dialRotator.solvePartOne(st.fileName);
    }

    @Benchmark
    public void dialRotator2_part1(St st) {
        st.dialRotator2.solvePartOne(st.fileName);
    }

    @Benchmark
    public void dialRotator3_part1(St st) {
        st.dialRotator3.solvePartOne(st.fileName);
    }

    // Part 2 benchmarks - grouped for easier comparison
    @Benchmark
    public void dialRotator_part2(St st) {
        st.dialRotator.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void dialRotator2_part2(St st) {
        st.dialRotator2.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void dialRotator3_part2(St st) {
        st.dialRotator3.solvePartTwo(st.fileName);
    }
}

