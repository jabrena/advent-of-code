package info.jab.aoc2025.day3;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day3Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        MaxJoltage maxJoltage = new MaxJoltage();
        String fileName = "/day3/day3-input.txt";
    }

    @Benchmark
    public void maxJoltage_part1(St st) {
        st.maxJoltage.solvePartOne(st.fileName);
    }

    @Benchmark
    public void maxJoltage_part2(St st) {
        st.maxJoltage.solvePartTwo(st.fileName);
    }
}

