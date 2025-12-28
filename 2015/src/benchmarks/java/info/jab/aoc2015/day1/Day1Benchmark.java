package info.jab.aoc2015.day1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day1Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day1 day1 = new Day1();
        String fileName = "/day1/day1-input.txt";
    }

    @Benchmark
    public void day1_part1(St st) {
        st.day1.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day1_part2(St st) {
        st.day1.getPart2Result(st.fileName);
    }
}

