package info.jab.aoc2015.day21;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day21Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        Day21 day21 = new Day21();
        String fileName = "/day21/day21-input.txt";
    }

    @Benchmark
    public void day21_part1(St st) {
        st.day21.getPart1Result(st.fileName);
    }

    @Benchmark
    public void day21_part2(St st) {
        st.day21.getPart2Result(st.fileName);
    }
}
