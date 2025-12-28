# AOC 2015

## How to test

```bash
# Compile first
./mvnw clean test -pl 2015
```

## Benchmarks & profiling

Benchmarks are located in `src/benchmarks` and are excluded from normal test runs. Use the `benchmarks` profile to run them.

```bash
# Day 1 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day1.Day1BenchmarkTest -D2015-day1-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 2 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day2.Day2BenchmarkTest -D2015-day2-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 3 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day3.Day3BenchmarkTest -D2015-day3-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 4 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day4.Day4BenchmarkTest -D2015-day4-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 5 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day5.Day5BenchmarkTest -D2015-day5-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 6 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day6.Day6BenchmarkTest -D2015-day6-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 7 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day7.Day7BenchmarkTest -D2015-day7-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 8 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day8.Day8BenchmarkTest -D2015-day8-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 9 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day9.Day9BenchmarkTest -D2015-day9-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 10 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day10.Day10BenchmarkTest -D2015-day10-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 11 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day11.Day11BenchmarkTest -D2015-day11-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 12 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day12.Day12BenchmarkTest -D2015-day12-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 13 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day13.Day13BenchmarkTest -D2015-day13-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 14 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day14.Day14BenchmarkTest -D2015-day14-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 15 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day15.Day15BenchmarkTest -D2015-day15-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 16 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day16.Day16BenchmarkTest -D2015-day16-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 17 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day17.Day17BenchmarkTest -D2015-day17-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 18 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day18.Day18BenchmarkTest -D2015-day18-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 19 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day19.Day19BenchmarkTest -D2015-day19-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 20 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day20.Day20BenchmarkTest -D2015-day20-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 21 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day21.Day21BenchmarkTest -D2015-day21-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 22 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day22.Day22BenchmarkTest -D2015-day22-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 23 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day23.Day23BenchmarkTest -D2015-day23-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 24 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day24.Day24BenchmarkTest -D2015-day24-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am

# Day 25 Benchmark (JMH)
./mvnw clean test -Pbenchmarks -Dtest=info.jab.aoc2015.day25.Day25BenchmarkTest -D2015-day25-benchmark=true -Dsurefire.failIfNoSpecifiedTests=false -pl 2015 -am
```
