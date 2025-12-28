package info.jab.aoc2015.day2;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day2Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day2 day2 = new Day2();
        String fileName = "/day2/day2-input.txt";
    }

    @Benchmark
    public void day2_part1(St st) {
        st.day2.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day2_part2(St st) {
        st.day2.getPart2Result(st.fileName);
    }
}

