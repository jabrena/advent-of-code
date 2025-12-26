package info.jab.aoc2025.day8;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day8Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        PointCluster pointCluster = new PointCluster();
        PointCluster2 pointCluster2 = new PointCluster2();
        String fileName = "/day8/day8-input.txt";
        Integer connectionLimit = 1000;
    }

    @Benchmark
    public void pointCluster_part1(St st) {
        st.pointCluster.solvePartOne(st.fileName, st.connectionLimit);
    }

    @Benchmark
    public void pointCluster2_part1(St st) {
        st.pointCluster2.solvePartOne(st.fileName, st.connectionLimit);
    }

    @Benchmark
    public void pointCluster_part2(St st) {
        st.pointCluster.solvePartTwo(st.fileName, st.connectionLimit);
    }

    @Benchmark
    public void pointCluster2_part2(St st) {
        st.pointCluster2.solvePartTwo(st.fileName, st.connectionLimit);
    }
}

