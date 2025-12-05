package info.jab.aoc2024.day1;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@Disabled
class AlternativesBenchmarkTest {

    @Test
    void should_show_best_alternatives() throws RunnerException {

        Options options = new OptionsBuilder()
                .include(AlternativeTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .result("target/jmh-results.json")
                //.verbosity(VerboseMode.EXTRA)
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
                .jvmArgs("-Xmx6144m", "-Xms6144m")
                //.addProfiler(StackProfiler.class)
                //.addProfiler(GCProfiler.class)
                //.addProfiler(LinuxPerfProfiler.class)
                //.addProfiler(ClassloaderProfiler.class)
                //.addProfiler(JavaFlightRecorderProfiler.class)
                .build();

        new Runner(options).run();
    }

}
