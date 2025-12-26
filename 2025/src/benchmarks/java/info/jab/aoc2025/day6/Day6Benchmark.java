package info.jab.aoc2025.day6;

import com.putoet.resources.ResourceLines;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.List;

public class Day6Benchmark { //NOSONAR java:S5786 - JMH @State annotation requires public class

    @State(Scope.Thread)
    public static class St {
        MathBlock mathBlock = new MathBlock();
        MathBlock2 mathBlock2 = new MathBlock2();
        String fileName = "/day6/day6-input.txt";
        List<String> lines = ResourceLines.list("/day6/day6-input.txt");
    }

    @Benchmark
    public void mathBlock_part1(St st) {
        st.mathBlock.solve(st.lines, st.mathBlock::processBlockPart1);
    }

    @Benchmark
    public void mathBlock2_part1(St st) {
        // Use pre-loaded lines for fair comparison (MathBlock2's solve method accepts lines)
        st.mathBlock2.solve(st.lines, range -> st.mathBlock2.processBlockPart1(st.lines, range));
    }

    @Benchmark
    public void mathBlock_part2(St st) {
        st.mathBlock.solve(st.lines, st.mathBlock::processBlockPart2);
    }

    @Benchmark
    public void mathBlock2_part2(St st) {
        // Use pre-loaded lines for fair comparison (MathBlock2's solve method accepts lines)
        st.mathBlock2.solve(st.lines, range -> st.mathBlock2.processBlockPart2(st.lines, range));
    }
}

