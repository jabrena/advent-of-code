package info.jab.aoc2015.day24;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day24Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day24 day24 = new Day24();
        String fileName = "/day24/day24-input.txt";
    }

    @Benchmark
    public void day24_part1(St st) {
        st.day24.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day24_part2(St st) {
        st.day24.getPart2Result(st.fileName);
    }
}
