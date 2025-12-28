package info.jab.aoc2015.day23;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day23Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day23 day23 = new Day23();
        String fileName = "/day23/day23-input.txt";
    }

    @Benchmark
    public void day23_part1(St st) {
        st.day23.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day23_part2(St st) {
        st.day23.getPart2Result(st.fileName);
    }
}
