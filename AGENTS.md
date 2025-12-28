# AGENTS.md - Guide for AI Models

This document helps AI models understand how to locate and use reports, tests, and benchmarks in this Advent of Code collection project.

## Project Structure

This is a multi-module Maven project organized by year:
- Each year (2015-2025) is a separate Maven module
- Main source code: `{YEAR}/src/main/java/info/jab/aoc{YEAR}/day{X}/`
- Test code: `{YEAR}/src/test/java/info/jab/aoc{YEAR}/day{X}/`
- Benchmark code: `{YEAR}/src/benchmarks/java/info/jab/aoc{YEAR}/day{X}/` (2025 only)
- Test resources: `{YEAR}/src/test/resources/day{X}/`
- Benchmark resources: `{YEAR}/src/benchmarks/resources/benchmarks/day{X}/` (2025 only)

## Reports

### Big O Notation Analysis

**Location**: `documentation/big-o-notation/{YEAR}.md`

**Purpose**: Time complexity analysis for all days in a given year.

**Contents**:
- Big O notation for Part 1 and Part 2 of each day
- Test execution times
- Notes on optimizations
- Improvement recommendations

**How to Use**:
1. Navigate to the year-specific file (e.g., `documentation/big-o-notation/2025.md`)
2. Find the day you're interested in
3. Review complexity analysis and optimization notes

## Tests

### Test Structure

**Location Pattern**: `{YEAR}/src/test/java/info/jab/aoc{YEAR}/day{X}/Day{X}Test.java`

**Naming Convention**: `Day{X}Test.java` where `{X}` is the day number (e.g., `Day1Test.java`)

**Test Structure**:
- Tests follow JUnit 5 conventions
- Each test class typically contains:
  - `should_solve_day{X}_part1()` - Tests Part 1 solution
  - `should_solve_day{X}_part2()` - Tests Part 2 solution
- Uses AssertJ for assertions (`then(result).isEqualTo(expected)`)
- Test data is loaded from resources: `/day{X}/day{X}-input.txt`

**Example Test File**:
```java
package info.jab.aoc2025.day1;

import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

class Day1Test {
    @Test
    void should_solve_day1_part1() {
        String fileName = "/day1/day1-input.txt";
        var day1 = new Day1();
        var result = day1.getPart1Result(fileName);
        then(result).isEqualTo(1034);
    }
}
```

### Running Tests

**Run all tests for a year**:
```bash
./mvnw clean test -pl {YEAR}
```

**Run tests for a specific day**:
```bash
./mvnw test -Dtest=info.jab.aoc{YEAR}.day{X}.Day{X}Test -pl {YEAR}
```

**Example**:
```bash
# Run all 2025 tests
./mvnw clean test -pl 2025

# Run Day 1 tests only
./mvnw test -Dtest=info.jab.aoc2025.day1.Day1Test -pl 2025
```

### Finding Tests

**Search for test files**:
```bash
# Find all test files for a year
find {YEAR}/src/test -name "*Test.java"

# Find test for a specific day
find {YEAR}/src/test -name "Day{X}Test.java"
```

**In codebase search**:
- Search for: `class Day{X}Test` or `Day{X}Test.java`
- Test files are in: `{YEAR}/src/test/java/info/jab/aoc{YEAR}/day{X}/`

## Benchmarks

### Benchmark Structure (2025 Module Only)

**Location Pattern**:
- Benchmark classes: `2025/src/benchmarks/java/info/jab/aoc2025/day{X}/Day{X}Benchmark.java`
- Benchmark tests: `2025/src/benchmarks/java/info/jab/aoc2025/day{X}/Day{X}BenchmarkTest.java`
- Benchmark results: `2025/src/benchmarks/resources/benchmarks/day{X}/`

**Naming Convention**:
- Benchmark class: `Day{X}Benchmark.java`
- Benchmark test runner: `Day{X}BenchmarkTest.java`

**Benchmark Structure**:
- Uses JMH (Java Microbenchmark Harness)
- Benchmarks are disabled by default (use `@DisabledIf` annotation)
- Each benchmark compares different implementations of the same problem
- Results are saved as JSON files
- Includes Java Flight Recorder (JFR) profiling data

**Example Benchmark File**:
```java
package info.jab.aoc2025.day1;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class Day1Benchmark {
    @State(Scope.Thread)
    public static class St {
        DialRotator dialRotator = new DialRotator();
        DialRotator2 dialRotator2 = new DialRotator2();
        DialRotator3 dialRotator3 = new DialRotator3();
        String fileName = "/day1/day1-input.txt";
        List<String> lines = ResourceLines.list("/day1/day1-input.txt");
    }

    @Benchmark
    public void dialRotator_part1(St st) {
        st.dialRotator.solve(st.lines, st.dialRotator::processPart1);
    }
}
```

### Running Benchmarks

**General Pattern**:
```bash
./mvnw clean test -Pbenchmarks \
  -Dtest=info.jab.aoc2025.day{X}.Day{X}BenchmarkTest \
  -D2025-day{X}-benchmark=true \
  -Dsurefire.failIfNoSpecifiedTests=false \
  -pl 2025 -am
```

**Available Benchmarks (2025)**:
- Day 1: Compares `DialRotator`, `DialRotator2`, `DialRotator3`
- Day 2: Compares `InvalidIdValidator`, `InvalidIdValidator2`, `InvalidIdValidator3`
- Day 3: Measures `MaxJoltage` performance
- Day 4: Compares `GridNeighbor`, `GridNeighbor2`
- Day 5: Compares `Range`, `Range2`, `Range3`
- Day 6: Compares `MathBlock`, `MathBlock2`
- Day 7: Measures `BeamPathCounter` performance
- Day 8: Compares `PointCluster`, `PointCluster2`
- Day 9: Measures `MaxRectangleArea` performance
- Day 10: Measures `ButtonPressOptimizer` performance
- Day 11: Measures `GraphPathCounter` performance
- Day 12: Measures `ShapePacking` performance

**Example - Run Day 1 Benchmark**:
```bash
./mvnw clean test -Pbenchmarks \
  -Dtest=info.jab.aoc2025.day1.Day1BenchmarkTest \
  -D2025-day1-benchmark=true \
  -Dsurefire.failIfNoSpecifiedTests=false \
  -pl 2025 -am
```

### Benchmark Results

**Result Files**:
- JSON results: `2025/src/benchmarks/resources/benchmarks/day{X}/Day{X}Benchmark.json`
- JFR profiles: `2025/src/benchmarks/resources/benchmarks/day{X}/info.jab.aoc2025.day{X}.Day{X}Benchmark.{method}-AverageTime/profile.jfr`
- GC logs: `2025/src/benchmarks/resources/benchmarks/day{X}/gc.log`
- Heap dumps (if OOM): `2025/src/benchmarks/resources/benchmarks/day{X}/heap-dump.hprof`

**Interpreting Results**:
1. **JSON Results**: Contains timing data for each benchmark method
2. **JFR Profiles**: Can be opened in JDK Mission Control for detailed performance analysis
3. **GC Logs**: Contains garbage collection information

**Finding Benchmark Results**:
```bash
# List all benchmark results
find 2025/src/benchmarks/resources/benchmarks -name "*.json"

# View a specific benchmark result
cat 2025/src/benchmarks/resources/benchmarks/day1/Day1Benchmark.json
```

### Finding Benchmarks

**Search for benchmark files**:
```bash
# Find all benchmark classes
find 2025/src/benchmarks -name "*Benchmark.java"

# Find benchmark for a specific day
find 2025/src/benchmarks -path "*/day{X}/*Benchmark.java"
```

**In codebase search**:
- Search for: `class Day{X}Benchmark` or `Day{X}Benchmark.java`
- Benchmark files are in: `2025/src/benchmarks/java/info/jab/aoc2025/day{X}/`

## Quick Reference

### File Location Patterns

| Type | Pattern | Example |
|------|---------|---------|
| Main Code | `{YEAR}/src/main/java/info/jab/aoc{YEAR}/day{X}/Day{X}.java` | `2025/src/main/java/info/jab/aoc2025/day1/Day1.java` |
| Test | `{YEAR}/src/test/java/info/jab/aoc{YEAR}/day{X}/Day{X}Test.java` | `2025/src/test/java/info/jab/aoc2025/day1/Day1Test.java` |
| Benchmark | `2025/src/benchmarks/java/info/jab/aoc2025/day{X}/Day{X}Benchmark.java` | `2025/src/benchmarks/java/info/jab/aoc2025/day1/Day1Benchmark.java` |
| Test Input | `{YEAR}/src/test/resources/day{X}/day{X}-input.txt` | `2025/src/test/resources/day1/day1-input.txt` |
| Benchmark Results | `2025/src/benchmarks/resources/benchmarks/day{X}/Day{X}Benchmark.json` | `2025/src/benchmarks/resources/benchmarks/day1/Day1Benchmark.json` |

### Common Commands

```bash
# Run all tests for a year
./mvnw clean test -pl {YEAR}

# Run specific test
./mvnw test -Dtest=info.jab.aoc{YEAR}.day{X}.Day{X}Test -pl {YEAR}

# Run benchmark (2025 only)
./mvnw clean test -Pbenchmarks \
  -Dtest=info.jab.aoc2025.day{X}.Day{X}BenchmarkTest \
  -D2025-day{X}-benchmark=true \
  -Dsurefire.failIfNoSpecifiedTests=false \
  -pl 2025 -am

# Find test files
find {YEAR}/src/test -name "*Test.java"

# Find benchmark files (2025 only)
find 2025/src/benchmarks -name "*Benchmark.java"
```

## Best Practices for AI Models

1. **When analyzing code**:
   - First check if tests exist: `{YEAR}/src/test/java/info/jab/aoc{YEAR}/day{X}/Day{X}Test.java`
   - Read tests to understand expected behavior
   - Check for benchmark results if performance is a concern

2. **When implementing solutions**:
   - Follow the test structure pattern
   - Use the same naming conventions
   - Place test files in the correct package structure

3. **When searching for information**:
   - Use semantic search for concepts (e.g., "How does Day1 solve the problem?")
   - Use grep for exact matches (e.g., class names, method names)
   - Check documentation folders for analysis documents

4. **When working with benchmarks**:
   - Benchmarks are disabled by default - enable with system property
   - Always use the `benchmarks` Maven profile
   - Review JSON results and JFR profiles for detailed analysis

## Libraries and Dependencies

This project uses several libraries to enhance functionality, performance, and code quality. Understanding these libraries helps when reading, modifying, or creating new solutions.

### Core Libraries

#### 1. **Putoet AOC** (`com.putoet:aoc`)
**Version**: 1.7.0
**Purpose**: Utility library for Advent of Code problems

**Key Classes**:
- `ResourceLines`: Utility for reading input files from resources
- `Grid`, `GridUtils`, `Point`: Grid manipulation utilities
- `GridDirections`: Direction constants for grid navigation

**Usage Examples**:
```java
import com.putoet.resources.ResourceLines;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import com.putoet.grid.Point;

// Read input lines
List<String> lines = ResourceLines.list("/day1/day1-input.txt");

// Create grid from lines
Grid grid = new Grid(GridUtils.of(lines));

// Find all points matching a condition
List<Point> points = grid.findAll(c -> c == '@');
```

**Where Used**: Most days use `ResourceLines` for file I/O. Days 4, 7 use grid utilities.

---

#### 2. **Eclipse Collections** (`org.eclipse.collections:eclipse-collections`)
**Version**: 13.0.0
**Purpose**: High-performance collections framework with rich APIs

**Key Features**:
- Primitive collections (avoids boxing overhead)
- Rich functional APIs
- Memory-efficient implementations
- Mutable and immutable variants

**Usage Examples**:
```java
import org.eclipse.collections.impl.map.mutable.primitive.LongIntHashMap;

// Primitive map (no boxing)
LongIntHashMap map = new LongIntHashMap(1 << 16);
map.put(0L, 0);
int value = map.getIfAbsent(key, Integer.MAX_VALUE);
```

**Where Used**:
- Day 10 (`Part1Solver.java`): Uses `LongIntHashMap` for meet-in-the-middle algorithm
- Day 1 (`DialRotator2.java`): Uses Eclipse Collections Lists for DataFrame operations

**Benefits**:
- Eliminates boxing/unboxing overhead
- Better memory efficiency
- Rich functional APIs

---

#### 3. **FastUtil** (`it.unimi.dsi:fastutil`)
**Version**: 8.5.12
**Purpose**: Fast and compact type-specific collections for primitives

**Key Classes**:
- `IntList`, `LongList`: Primitive lists
- `IntArrayList`, `LongArrayList`: Primitive array lists
- `ObjectList`, `ObjectArrayList`: Object lists with optimized implementations

**Usage Examples**:
```java
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

// Primitive lists (no boxing)
LongList ids = new LongArrayList();
ids.add(123L);

// Object lists (optimized)
ObjectList<Edge> edges = new ObjectArrayList<>();
edges.add(new Edge(0, 1, 10));
```

**Where Used**:
- Day 3 (`Bank.java`): `IntArrayList` for bank operations
- Day 5 (`Range3.java`): `LongArrayList` for ID collections
- Day 8 (`PointCluster3.java`): `ObjectArrayList` for points and edges, `IntList` for component sizes
- Day 8 (`DSU.java`): `IntArrayList` for union-find operations

**Benefits**:
- Eliminates boxing/unboxing overhead
- Better cache locality
- Reduced memory footprint
- Optimized for performance-critical code

---

#### 4. **DataFrame-EC** (`io.github.vmzakharov:dataframe-ec`)
**Version**: 1.4.0
**Purpose**: Tabular data structure built on Eclipse Collections for columnar data manipulation

**Key Features**:
- Functional operations on tabular data
- Single-pass processing
- Direct aggregation
- Lazy evaluation support

**Usage Examples**:
```java
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import org.eclipse.collections.api.factory.Lists;

// Create DataFrame
DataFrame df = new DataFrame("Rotations")
    .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

// Single-pass processing with direct aggregation
df.collect(row -> {
    // Process row and aggregate
});
```

**Where Used**:
- Day 1 (`DialRotator2.java`): Pure DataFrame-oriented solution with single-pass processing
- Day 2 (`InvalidIdValidator3.java`): Lazy on-demand processing with DataFrame collect

**Benefits**:
- Declarative data processing
- Single-pass algorithms
- Functional programming style
- Built on Eclipse Collections for performance

---

#### 5. **Guava** (`com.google.guava:guava`)
**Version**: 33.5.0-jre
**Purpose**: Google's core libraries for Java

**Key Features**:
- Collections utilities
- Caching
- String utilities
- Functional programming support

**Note**: Available in project but usage is minimal. Check codebase for specific usage patterns.

---

#### 6. **Apache Commons Lang3** (`org.apache.commons:commons-lang3`)
**Version**: 3.20.0
**Purpose**: Apache Commons utilities for string manipulation, reflection, and more

**Note**: Available in project but usage is minimal. Check codebase for specific usage patterns.

---

#### 7. **CombinatoricsLib3** (`com.github.dpaukov:combinatoricslib3`)
**Version**: 3.4.0
**Purpose**: Library for generating combinations, permutations, and other combinatorial objects

**Note**: Available in project. Check codebase for specific usage patterns.

---

#### 8. **Parallel Collectors** (`com.pivovarit:parallel-collectors`)
**Version**: 3.4.0
**Purpose**: Enhanced parallel stream collectors for better parallel processing

**Note**: Available in project.

---

#### 9. **Agrona** (`org.agrona:agrona`)
**Version**: 2.3.1
**Purpose**: High-performance data structures and utilities

**Note**: Available in project. Check codebase for specific usage patterns.

---

### Testing Libraries

#### 1. **JUnit 5** (`org.junit.jupiter:junit-jupiter`)
**Version**: 5.14.1
**Purpose**: Modern testing framework for Java

**Key Features**:
- `@Test` annotations
- Parameterized tests
- Test lifecycle management
- Extensions

**Usage**: All test files use JUnit 5.

---

#### 2. **AssertJ** (`org.assertj:assertj-core`)
**Version**: 3.27.6
**Purpose**: Fluent assertions for Java

**Usage Pattern**:
```java
import static org.assertj.core.api.BDDAssertions.then;

then(result).isEqualTo(expected);
```

**Where Used**: All test files use AssertJ for assertions.

---

#### 3. **JMH** (`org.openjdk.jmh:jmh-core`)
**Version**: 1.37
**Purpose**: Java Microbenchmark Harness for performance testing

**Key Annotations**:
- `@Benchmark`: Marks benchmark methods
- `@State`: Defines benchmark state
- `@Param`: Parameterized benchmarks

**Usage**: All benchmark files in `2025/src/benchmarks/` use JMH.

---

#### 4. **Allure** (`io.qameta.allure:allure-junit5`)
**Version**: 2.32.0
**Purpose**: Test reporting framework

**Usage**: Integrated for test reporting and visualization.

---

### Performance and Profiling Libraries

#### 1. **JOL** (`org.openjdk.jol:jol-core`)
**Version**: 0.17
**Purpose**: Java Object Layout - analyze object memory layout

**Note**: Available in project. Check codebase for specific usage patterns.

---

#### 2. **Java Flight Recorder (JFR)**
**Purpose**: Built-in JDK profiling tool

**Usage**: Integrated in benchmarks for detailed performance analysis. Results saved as `.jfr` files.

---

### UI Libraries

#### 1. **JavaFX** (`org.openjfx:javafx-controls`, `javafx-graphics`)
**Version**: 25.0.1
**Purpose**: JavaFX for visualizations

**Usage**: Used for visualizations in 2025 module:
- Day 1: `Day1Visualization`
- Day 3: `Day3Visualization`
- Day 4: `Day4Visualization`
- Day 5: `RangeVisualization`
- Day 7: `BeamPathVisualization`
- Day 8: `PointClusterVisualization`
- Day 9: `RectangleAreaVisualization`

**Running Visualizations**:
```bash
./mvnw -pl 2025 exec:java -Dexec.mainClass="info.jab.aoc2025.day{X}.{VisualizationClass}" -Dexec.classpathScope=test
```

---

### Data Processing Libraries

#### 1. **Jackson** (`com.fasterxml.jackson.core:jackson-databind`)
**Version**: 2.20.1
**Purpose**: JSON processing library

**Note**: Available in project. Check codebase for specific usage patterns.

---

### Library Selection Guidelines

When choosing libraries for new solutions:

1. **Performance-Critical Code**:
   - Use **FastUtil** for primitive collections
   - Use **Eclipse Collections** for rich APIs with performance benefits
   - Consider **Agrona** for high-performance data structures

2. **Data Processing**:
   - Use **DataFrame-EC** for tabular data operations
   - Use **Eclipse Collections** for functional transformations

3. **Grid/2D Problems**:
   - Use **Putoet Grid** utilities (`Grid`, `GridUtils`, `Point`)

4. **File I/O**:
   - Use **Putoet ResourceLines** for reading input files

5. **Testing**:
   - Use **JUnit 5** for test structure
   - Use **AssertJ** for fluent assertions
   - Use **JMH** for performance benchmarks

6. **Visualization**:
   - Use **JavaFX** for interactive visualizations

### Finding Library Usage

**Search for library imports**:
```bash
# Find FastUtil usage
grep -r "import it.unimi.dsi.fastutil" 2025/src/main

# Find Eclipse Collections usage
grep -r "import org.eclipse.collections" 2025/src/main

# Find DataFrame usage
grep -r "import.*dataframe" 2025/src/main

# Find Putoet utilities
grep -r "import com.putoet" 2025/src/main
```

**Check dependencies**:
```bash
# View all dependencies
./mvnw dependency:tree -pl 2025

# View dependency versions
cat pom.xml | grep -A 1 "version>"
```

## Additional Resources

- **Project README**: `README.md` - General project information
- **Year-specific README**: `{YEAR}/README.md` - Year-specific instructions
- **Documentation**: `documentation/` - Additional analysis and documentation
- **Big O Analysis**: `documentation/big-o-notation/{YEAR}.md` - Complexity analysis

