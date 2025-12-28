package info.jab.aoc2025.day7;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day7Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        BeamPathCounter beamPathCounter = new BeamPathCounter();
        String fileName = "/day7/day7-input.txt";
    }

    @Benchmark
    public void beamPathCounter_part1(St st) {
        st.beamPathCounter.solvePartOne(st.fileName);
    }

    @Benchmark
    public void beamPathCounter_part2(St st) {
        st.beamPathCounter.solvePartTwo(st.fileName);
    }
}

