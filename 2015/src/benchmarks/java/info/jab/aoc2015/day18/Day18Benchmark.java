package info.jab.aoc2015.day18;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day18Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day18 day18 = new Day18();
        String fileName = "/day18/day18-input.txt";
    }

    @Benchmark
    public void day18_part1(St st) {
        st.day18.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day18_part2(St st) {
        st.day18.getPart2Result(st.fileName);
    }
}
