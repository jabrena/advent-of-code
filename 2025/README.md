# AOC 2025

## How to test

```bash
# Compile first
./mvnw clean test -pl 2025
```

## Benchmarks & profiling

Benchmarks are located in `src/benchmarks` and are excluded from normal test runs. Use the `benchmarks` profile to run them.

```bash
# Day 1 Benchmark (JMH)
# Run benchmark to compare DialRotator vs DialRotator2 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day1.Day1BenchmarkTest -D2025-day1-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am

# Day 2 Benchmark (JMH)
# Run benchmark to compare InvalidIdValidator vs InvalidIdValidator2 vs InvalidIdValidator3 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day2.Day2BenchmarkTest -D2025-day2-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am

# Day 4 Benchmark (JMH)
# Run benchmark to compare GridNeighbor vs GridNeighbor2 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day4.Day4BenchmarkTest -D2025-day4-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am

# Day 5 Benchmark (JMH)
# Run benchmark to compare Range vs Range2 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day5.Day5BenchmarkTest -D2025-day5-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am

# Day 6 Benchmark (JMH)
# Run benchmark to compare MathBlock vs MathBlock2 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day6.Day6BenchmarkTest -D2025-day6-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am

# Day 8 Benchmark (JMH)
# Run benchmark to compare PointCluster vs PointCluster2 implementations:
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2025.day8.Day8BenchmarkTest -D2025-day8-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2025 -am
```

## Visualizations

```bash
# JavaFX requires module path configuration - use Maven exec plugin:
# Day 1
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day1.Day1Visualization" -Dexec.classpathScope=test

# Day 3
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day3.Day3Visualization" -Dexec.classpathScope=test

# Day 4
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day4.Day4Visualization" -Dexec.classpathScope=test

# Day 5
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day5.RangeVisualization" -Dexec.classpathScope=test

# Day 7
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day7.BeamPathVisualization" -Dexec.classpathScope=test

# Day 8
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day8.PointClusterVisualization" -Dexec.classpathScope=test

# Day 9
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day9.RectangleAreaVisualization" -Dexec.classpathScope=test
```
