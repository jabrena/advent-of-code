package info.jab.aoc2015.day25;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day25Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day25 day25 = new Day25();
        String fileName = "/day25/day25-input.txt";
    }

    @Benchmark
    public void day25_part1(St st) {
        st.day25.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day25_part2(St st) {
        st.day25.getPart2Result(st.fileName);
    }
}
