package info.jab.aoc2015.day6;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day6Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day6 day6 = new Day6();
        String fileName = "/day6/day6-input.txt";
    }

    @Benchmark
    public void day6_part1(St st) {
        st.day6.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day6_part2(St st) {
        st.day6.getPart2Result(st.fileName);
    }
}
