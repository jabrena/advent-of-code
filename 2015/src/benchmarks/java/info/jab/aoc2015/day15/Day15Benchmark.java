package info.jab.aoc2015.day15;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day15Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day15 day15 = new Day15();
        String fileName = "/day15/day15-input.txt";
    }

    @Benchmark
    public void day15_part1(St st) {
        st.day15.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day15_part2(St st) {
        st.day15.getPart2Result(st.fileName);
    }
}
