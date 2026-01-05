package info.jab.aoc2015.day9;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day9Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day9 day9 = new Day9();
        String fileName = "/day9/day9-input.txt";
    }

    @Benchmark
    public void day9_part1(St st) {
        st.day9.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day9_part2(St st) {
        st.day9.getPart2Result(st.fileName);
    }
}
