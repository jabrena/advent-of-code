package info.jab.aoc.day1;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

// WIP
// TODO Review this idea
@Disabled
@State(Scope.Thread)
@Threads(1)
public class AlternativesBenchmark2Test {

    @Setup(Level.Invocation)
    public void setupInvokation() throws Exception {
        // executed before each invocation of the benchmark
    }

    @Setup(Level.Iteration)
    public void setupIteration() throws Exception {
        // executed before each invocation of the iteration
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 1, value = 1)
    @Warmup(batchSize = -1, iterations = 3, time = 10, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(batchSize = -1, iterations = 10, time = 10, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test() throws Exception {
        HistorianHysteria historianHysteria = new HistorianHysteria();
        String fileName = "/day1/day1-input.txt";
        historianHysteria.loadFle.andThen(historianHysteria.splitInto2Lists).apply(fileName);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(warmups = 1, value = 1)
    @Warmup(batchSize = -1, iterations = 3, time = 10, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(batchSize = -1, iterations = 10, time = 10, timeUnit = TimeUnit.MILLISECONDS)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void test2() throws Exception {
        HistorianHysteria historianHysteria = new HistorianHysteria();
        String fileName = "/day1/day1-input.txt";
        historianHysteria.loadFle.andThen(historianHysteria.splitInto2Lists2).apply(fileName);
    }

    @Test
    public void benchmark() throws Exception {
        String[] argv = {};
        org.openjdk.jmh.Main.main(argv);
    }
}
