package info.jab.aoc2015.day16;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day16Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day16 day16 = new Day16();
        String fileName = "/day16/day16-input.txt";
    }

    @Benchmark
    public void day16_part1(St st) {
        st.day16.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day16_part2(St st) {
        st.day16.getPart2Result(st.fileName);
    }
}
