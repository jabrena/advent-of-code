package info.jab.aoc2015.day22;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day22Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day22 day22 = new Day22();
        String fileName = "/day22/day22-input.txt";
    }

    @Benchmark
    public void day22_part1(St st) {
        st.day22.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day22_part2(St st) {
        st.day22.getPart2Result(st.fileName);
    }
}
