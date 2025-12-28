package info.jab.aoc2015.day17;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day17Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day17 day17 = new Day17();
        String fileName = "/day17/day17-input.txt";
    }

    @Benchmark
    public void day17_part1(St st) {
        st.day17.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day17_part2(St st) {
        st.day17.getPart2Result(st.fileName);
    }
}
