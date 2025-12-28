package info.jab.aoc2015.day8;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day8Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day8 day8 = new Day8();
        String fileName = "/day8/day8-input.txt";
    }

    @Benchmark
    public void day8_part1(St st) {
        st.day8.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day8_part2(St st) {
        st.day8.getPart2Result(st.fileName);
    }
}
