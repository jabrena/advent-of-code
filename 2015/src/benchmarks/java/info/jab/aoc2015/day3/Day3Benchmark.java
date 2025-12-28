package info.jab.aoc2015.day3;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day3Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day3 day3 = new Day3();
        String fileName = "/day3/day3-input.txt";
    }

    @Benchmark
    public void day3_part1(St st) {
        st.day3.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day3_part2(St st) {
        st.day3.getPart2Result(st.fileName);
    }
}

