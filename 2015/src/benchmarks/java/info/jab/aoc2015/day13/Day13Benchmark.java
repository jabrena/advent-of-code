package info.jab.aoc2015.day13;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day13Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day13 day13 = new Day13();
        String fileName = "/day13/day13-input.txt";
    }

    @Benchmark
    public void day13_part1(St st) {
        st.day13.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day13_part2(St st) {
        st.day13.getPart2Result(st.fileName);
    }
}
