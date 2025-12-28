package info.jab.aoc2015.day14;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day14Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day14 day14 = new Day14();
        String fileName = "/day14/day14-input.txt";
    }

    @Benchmark
    public void day14_part1(St st) {
        st.day14.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day14_part2(St st) {
        st.day14.getPart2Result(st.fileName);
    }
}
