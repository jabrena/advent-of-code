package info.jab.aoc2015.day6;

import info.jab.aoc.DisabledIf;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;

/**
 * JMH Benchmark test - disabled by default.
 * Enable with: mvn test -D2015-day6-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am
 */
@DisabledIf(
    name = "2015-day6-benchmark",
    value = "false",
    reason = "Benchmark tests are disabled by default. Enable with: mvn test -D2015-day6-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am"
)
class Day6BenchmarkTest {

    @Test
    void should_benchmark_day6() throws RunnerException {

        Options options = new OptionsBuilder()
                .include(Day6Benchmark.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("src/benchmarks/resources/benchmarks/day6/%s.json".formatted(Day6Benchmark.class.getSimpleName()))
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MILLISECONDS)
                .warmupTime(TimeValue.seconds(5))
                .measurementTime(TimeValue.milliseconds(1))
                .measurementIterations(10)
                .threads(Runtime.getRuntime().availableProcessors())
                .warmupIterations(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .forks(3)
                .jvmArgs(
                        "-Xmx6144m",
                        "-Xms6144m",
                        "-Xlog:gc*:file=src/benchmarks/resources/benchmarks/day6/gc.log:time,tags,level:filecount=0",
                        "-XX:+HeapDumpOnOutOfMemoryError",
                        "-XX:HeapDumpPath=src/benchmarks/resources/benchmarks/day6/heap-dump.hprof",
                        "-XX:+UnlockDiagnosticVMOptions"
                )
                .addProfiler(JavaFlightRecorderProfiler.class, "dir=src/benchmarks/resources/benchmarks/day6;config=profile")
                .build();

        new Runner(options).run();
    }

}
