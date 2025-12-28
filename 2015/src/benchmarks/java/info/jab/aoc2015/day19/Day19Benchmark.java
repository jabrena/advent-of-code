package info.jab.aoc2015.day19;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day19Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day19 day19 = new Day19();
        String fileName = "/day19/day19-input.txt";
    }

    @Benchmark
    public void day19_part1(St st) {
        st.day19.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day19_part2(St st) {
        st.day19.getPart2Result(st.fileName);
    }
}
