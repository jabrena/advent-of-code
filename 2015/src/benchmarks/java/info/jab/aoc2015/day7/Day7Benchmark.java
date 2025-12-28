package info.jab.aoc2015.day7;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day7Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day7 day7 = new Day7();
        String fileName = "/day7/day7-input.txt";
    }

    @Benchmark
    public void day7_part1(St st) {
        st.day7.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day7_part2(St st) {
        st.day7.getPart2Result(st.fileName);
    }
}
