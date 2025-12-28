package info.jab.aoc2015.day12;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day12Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day12 day12 = new Day12();
        String fileName = "/day12/day12-input.txt";
    }

    @Benchmark
    public void day12_part1(St st) {
        st.day12.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day12_part2(St st) {
        st.day12.getPart2Result(st.fileName);
    }
}
