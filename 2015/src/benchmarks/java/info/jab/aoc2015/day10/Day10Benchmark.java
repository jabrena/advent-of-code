package info.jab.aoc2015.day10;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day10Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day10 day10 = new Day10();
        String fileName = "/day10/day10-input.txt";
    }

    @Benchmark
    public void day10_part1(St st) {
        st.day10.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day10_part2(St st) {
        st.day10.getPart2Result(st.fileName);
    }
}
