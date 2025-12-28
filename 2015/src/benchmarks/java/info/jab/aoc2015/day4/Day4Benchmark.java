package info.jab.aoc2015.day4;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day4Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day4 day4 = new Day4();
        String fileName = "/day4/day4-input.txt";
    }

    @Benchmark
    public void day4_part1(St st) {
        st.day4.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day4_part2(St st) {
        st.day4.getPart2Result(st.fileName);
    }
}

