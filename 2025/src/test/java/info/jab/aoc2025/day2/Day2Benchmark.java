package info.jab.aoc2025.day2;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day2Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        InvalidIdValidator invalidIdValidator = new InvalidIdValidator();
        InvalidIdValidator2 invalidIdValidator2 = new InvalidIdValidator2();
        InvalidIdValidator3 invalidIdValidator3 = new InvalidIdValidator3();
        String fileName = "/day2/day2-input.txt";
    }

    @Benchmark
    public void invalidIdValidator_part1(St st) {
        st.invalidIdValidator.solvePartOne(st.fileName);
    }

    @Benchmark
    public void invalidIdValidator2_part1(St st) {
        st.invalidIdValidator2.solvePartOne(st.fileName);
    }

    @Benchmark
    public void invalidIdValidator3_part1(St st) {
        st.invalidIdValidator3.solvePartOne(st.fileName);
    }

    @Benchmark
    public void invalidIdValidator_part2(St st) {
        st.invalidIdValidator.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void invalidIdValidator2_part2(St st) {
        st.invalidIdValidator2.solvePartTwo(st.fileName);
    }

    @Benchmark
    public void invalidIdValidator3_part2(St st) {
        st.invalidIdValidator3.solvePartTwo(st.fileName);
    }
}

