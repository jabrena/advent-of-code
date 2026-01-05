package info.jab.aoc2015.day20;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day20Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day20 day20 = new Day20();
        String fileName = "/day20/day20-input.txt";
    }

    @Benchmark
    public void day20_part1(St st) {
        st.day20.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day20_part2(St st) {
        st.day20.getPart2Result(st.fileName);
    }
}
