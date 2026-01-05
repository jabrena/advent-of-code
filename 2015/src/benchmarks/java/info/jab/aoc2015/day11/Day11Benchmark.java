package info.jab.aoc2015.day11;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day11Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day11 day11 = new Day11();
        String fileName = "/day11/day11-input.txt";
    }

    @Benchmark
    public void day11_part1(St st) {
        st.day11.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day11_part2(St st) {
        st.day11.getPart2Result(st.fileName);
    }
}
