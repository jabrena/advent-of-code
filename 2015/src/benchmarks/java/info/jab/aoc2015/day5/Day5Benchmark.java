package info.jab.aoc2015.day5;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day5Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day5 day5 = new Day5();
        String fileName = "/day5/day5-input.txt";
    }

    @Benchmark
    public void day5_part1(St st) {
        st.day5.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day5_part2(St st) {
        st.day5.getPart2Result(st.fileName);
    }
}
