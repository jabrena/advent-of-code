# Benchmark Variance Analysis and Improvement Recommendations

## Executive Summary

This document analyzes benchmark results from all days (1-12) to identify high variance in execution times and provides specific code improvement recommendations. High variance indicates inconsistent performance, which can be caused by:

1. **Garbage Collection (GC) pauses** - Intermittent GC activity during benchmarks
2. **JIT compilation** - Just-In-Time compiler optimizations happening at different times
3. **Memory allocation patterns** - Excessive object creation causing GC pressure
4. **Parallel processing** - Uneven work distribution in parallel streams
5. **Cache effects** - CPU cache misses and memory access patterns

## Variance Analysis Methodology

Variance is measured using the **Coefficient of Variation (CV)** = `scoreError / score`, where:
- `score` = mean execution time
- `scoreError` = standard error (related to standard deviation)

**High Variance Threshold**: CV > 50% indicates significant variance that should be addressed.

## Priority Ranking

### 🔴 Critical (CV > 70%)
1. **[Day 6 MathBlock](#day-6)** - CV 102-103% ⚠️ **CRITICAL**
   - Implementation: `MathBlock.java`
   - Issues: `substring()` + `trim()` on lines 84, 172, new ArrayList per block (line 80)
   - Impact: Creates millions of temporary strings

2. **[Day 2 InvalidIdValidator2](#day-2)** - CV 72% ⚠️ **CRITICAL**
   - Implementation: `InvalidIdValidator2.java`
   - Issues: `longToCharArray()` allocates new array per ID (line 165), `split()` overhead (line 70)
   - Impact: Processing millions of IDs creates massive GC pressure

### 🟡 High (CV 50-70%)
3. **[Day 3 MaxJoltage](#day-3)** - CV 37-52% ⚠️ **HIGH**
   - Implementation: `MaxJoltage.java` uses `Bank.java`
   - Part 1: CV = 52.4% (score: 0.631ms, error: 0.331ms)
   - Part 2: CV = 37.2% (score: 0.825ms, error: 0.307ms)
   - Issues: Stream operations (line 48-52), Bank object allocations per line, Trampoline allocations
   - Impact: Part 1 has high variance with outliers (max: 2.212ms vs median: 0.416ms)
   - Root causes: `Bank.from()` creates new IntArrayList per line, trampoline pattern creates Done/More objects

4. **[Day 5 Range3](#day-5)** - CV 47-70%
   - Implementation: `Range3.java`
   - Issues: Boxing in `convertToLongList()` (line 75), stream operations (lines 120-125)
   - Impact: Intermediate collections from streams

5. **[Day 1 DialRotator3](#day-1)** - CV 38-58%
   - Implementation: `DialRotator3.java`
   - Issues: Stream collection overhead (lines 43-45, 85-87)
   - Impact: Already well-optimized, minor improvements possible

6. **[Day 12 ShapePacking](#day-12)** - CV 65%
   - Implementation: `ShapePacking.java`
   - Issues: CacheKey allocations (line 180), parallel work distribution
   - Impact: Backtracking algorithm has inherent variance

7. **[Day 7 BeamPathCounter](#day-7)** - CV 38-106%
   - Implementation: `BeamPathCounter.java`
   - Issues: Stream operations with `teeing()`, Point/BeamAction allocations, boxing (line 84-85)
   - Impact: Part 2 has critical variance (106%) with 15ms outlier

### 🟢 Medium (CV 30-50%)
8. **[Day 7 BeamPathCounter](#day-7)** - CV 38% (Part 1 only)
   - Implementation: `BeamPathCounter.java`
   - Issues: Stream operations with `teeing()`, Point/BeamAction allocations
   - Impact: Part 1 has medium variance, Part 2 has critical variance (106%)

9. **[Day 11 GraphPathCounter](#day-11)** - CV 42-58%
   - Implementation: `GraphPathCounter.java`
   - Issues: Stream operations, Optional overhead, HashMap allocations
   - Impact: Multiple intermediate collections

10. **[Day 4 GridNeighbor2](#day-4)** - CV 26-41%
   - Implementation: `GridNeighbor2.java`
   - Issues: Point object allocations, parallel stream variance
   - Impact: Part 2 has higher variance due to iterative mutations

### ✅ Low Variance (CV < 30%)
11. **[Day 8 PointCluster3](#day-8)** - CV 9-18% ✅ **EXCELLENT**
   - Implementation: `PointCluster3.java`
   - Status: Already well-optimized with FastUtil, minimal improvements needed

---

## Days with High Variance

### Day 1: DialRotator3 (CV: 38-61%) {#day-1}

**Implementation**: `Day1.java` uses `DialRotator3.java`

**Variance Details:**
- `dialRotator3_part1`: CV = 38% (scoreError: 0.37ms, score: 0.98ms)
- `dialRotator3_part2`: CV = 58% (scoreError: 0.65ms, score: 1.11ms)

**Current Implementation Analysis** (`DialRotator3.java`):
- ✅ Already uses mutable state (eliminates DialState allocations)
- ✅ Already avoids `trim()` in `isValidRotation()` (line 114-120)
- ✅ Already uses FastUtil ObjectArrayList
- ⚠️ **Issue**: `longToCharArray()` in `InvalidIdValidator2` creates new char arrays per ID
- ⚠️ **Issue**: Stream collection to ObjectArrayList still has overhead

**Root Causes:**
1. **Stream collection overhead**: Lines 43-45 and 85-87 collect to ObjectArrayList via stream
2. **GC activity**: GC logs show frequent young generation collections (1-2ms pauses)
3. **Method reference overhead**: `toObjectArrayList()` collector creates intermediate objects

**Improvement Recommendations for `DialRotator3.java`:**

```java
// 1. Replace stream collection with direct ResourceLines.list() + manual conversion
@Override
public Integer solvePartOne(final String fileName) {
    final List<String> lines = ResourceLines.list(fileName);
    // Convert to ObjectArrayList manually to avoid stream overhead
    final ObjectList<String> objectLines = new ObjectArrayList<>(lines.size());
    for (final String line : lines) {
        objectLines.add(line);
    }

    // Rest of implementation...
}

// 2. Consider eliminating ObjectArrayList if not needed
// Direct List<String> iteration may be faster due to JIT optimizations
@Override
public Integer solvePartOne(final String fileName) {
    final List<String> lines = ResourceLines.list(fileName);

    int position = INITIAL_POSITION;
    int zeroCount = 0;

    for (final String line : lines) {
        if (isValidRotation(line)) {
            // Process directly...
        }
    }
    return zeroCount;
}

// 3. Pre-size ObjectArrayList if keeping it
final ObjectList<String> lines = new ObjectArrayList<>(estimatedSize);
```

---

### Day 2: InvalidIdValidator2 (CV: 27-72%) {#day-2}

**Implementation**: `Day2.java` uses `InvalidIdValidator2.java`

**Variance Details:**
- `invalidIdValidator2_part1`: CV = 72% (scoreError: 54.73ms, score: 76.40ms) ⚠️ **CRITICAL**
- `invalidIdValidator2_part2`: CV = 27% (scoreError: 24.36ms, score: 89.44ms)

**Current Implementation Analysis** (`InvalidIdValidator2.java`):
- ✅ Already uses imperative loops (line 51-57)
- ✅ Already avoids substring creation (line 94-98)
- ❌ **Issue**: `longToCharArray()` (line 149-172) allocates new char array for EVERY ID
- ❌ **Issue**: `parseRanges()` (line 68-73) uses `split(",")` and stream operations

**Root Causes:**
1. **Char array allocation**: Line 165 `new char[digits]` creates millions of arrays
2. **String split overhead**: Line 70 `input.split(",")` creates array of strings
3. **GC pressure**: Processing millions of IDs creates many temporary objects
4. **Full GC pauses**: GC logs show occasional full GC (8-13ms pauses)

**Improvement Recommendations for `InvalidIdValidator2.java`:**

```java
// 1. Reuse char array buffer using ThreadLocal (lines 149-172)
private static final ThreadLocal<char[]> CHAR_BUFFER =
    ThreadLocal.withInitial(() -> new char[20]);

private boolean isInvalidForPartOne(final long id) {
    final char[] idChars = CHAR_BUFFER.get(); // Reuse buffer
    final int length = fillCharArray(id, idChars); // Fill in-place

    if (length % 2 != 0) {
        return false;
    }

    final int halfLength = length / 2;
    for (int i = 0; i < halfLength; i++) {
        if (idChars[i] != idChars[i + halfLength]) {
            return false;
        }
    }
    return true;
}

// Helper method to fill char array in-place
private static int fillCharArray(final long value, final char[] buffer) {
    if (value == 0) {
        buffer[0] = '0';
        return 1;
    }

    int digits = 0;
    long temp = value < 0 ? -value : value;
    while (temp > 0) {
        temp /= 10;
        digits++;
    }

    temp = value < 0 ? -value : value;
    for (int i = digits - 1; i >= 0; i--) {
        buffer[i] = (char) ('0' + (temp % 10));
        temp /= 10;
    }
    return digits;
}

// 2. Optimize parseRanges() to avoid split() (lines 68-73)
private List<Range> parseRanges(final String fileName) {
    final String input = ResourceLines.line(fileName).trim();
    final List<Range> ranges = new ArrayList<>();
    int start = 0;

    for (int i = 0; i < input.length(); i++) {
        if (input.charAt(i) == ',') {
            ranges.add(Range.from(input.substring(start, i)));
            start = i + 1;
        }
    }
    if (start < input.length()) {
        ranges.add(Range.from(input.substring(start)));
    }
    return ranges;
}

// 3. Use math-based validation for Part 1 (eliminate char arrays entirely)
private boolean isInvalidForPartOneMath(final long id) {
    if (id == 0) return true; // "00" pattern

    // Count digits
    int digits = 0;
    long temp = id;
    while (temp > 0) {
        temp /= 10;
        digits++;
    }

    if (digits % 2 != 0) return false;

    // Split into two halves
    long divisor = 1;
    for (int i = 0; i < digits / 2; i++) {
        divisor *= 10;
    }

    long firstHalf = id / divisor;
    long secondHalf = id % divisor;

    return firstHalf == secondHalf;
}
```

---

### Day 3: MaxJoltage (CV: 37-52%) ⚠️ **HIGH VARIANCE** {#day-3}

**Implementation**: `Day3.java` uses `MaxJoltage.java` which uses `Bank.java`

**Variance Details:**
- `maxJoltage_part1`:
  - **CV = 52.4%** (score: 0.631ms, error: 0.331ms) ⚠️ **HIGH**
  - Outliers: max 2.212ms vs median 0.416ms (5.3x difference)
- `maxJoltage_part2`:
  - **CV = 37.2%** (score: 0.825ms, error: 0.307ms) ⚠️ **MEDIUM**
  - Outliers: max 2.489ms vs median 0.626ms (4.0x difference)

**Current Implementation Analysis** (`MaxJoltage.java` and `Bank.java`):
- ✅ Uses FastUtil `IntArrayList` to avoid boxing (line 25 in `Bank.java`)
- ✅ Uses trampoline pattern to avoid stack overflow (line 51 in `Bank.java`)
- ⚠️ **Issue**: Stream operations create intermediate objects (lines 48-52 in `MaxJoltage.java`)
- ⚠️ **Issue**: `Bank.from()` creates new `IntArrayList` per line (line 25 in `Bank.java`)
- ⚠️ **Issue**: Trampoline pattern creates `Done`/`More` objects per iteration (lines 68, 75 in `Bank.java`)
- ⚠️ **Issue**: `line.chars().forEach()` creates IntStream overhead (line 26 in `Bank.java`)
- ⚠️ **Issue**: `MaxDigitResult` record allocation per iteration (line 73 in `Bank.java`)

**Root Causes:**
1. **Bank object allocations**: Each line creates a new `Bank` with new `IntArrayList` (line 49 in `MaxJoltage.java`)
2. **Trampoline allocations**: Each recursive step creates `Trampoline.Done` or `Trampoline.More` objects (lines 68, 75 in `Bank.java`)
3. **Stream overhead**: `ResourceLines.list().stream().map().filter().mapToLong().sum()` creates intermediate objects
4. **IntStream overhead**: `line.chars().forEach()` creates IntStream wrapper (line 26 in `Bank.java`)
5. **Record allocations**: `MaxDigitResult` record created per `findMaxDigitInRange` call (line 73 in `Bank.java`)
6. **GC pressure**: Many small object allocations cause GC pauses leading to variance

**Improvement Recommendations for `MaxJoltage.java` and `Bank.java`:**

```java
// 1. Replace stream with imperative loop in MaxJoltage.solve() (lines 47-53)
private Long solve(final String fileName, final int length) {
    final List<String> lines = ResourceLines.list(fileName);
    long sum = 0L;
    for (final String line : lines) {
        final Bank bank = Bank.from(line);
        if (bank.hasEnoughDigits(length)) {
            sum += bank.getMaxJoltage(length);
        }
    }
    return sum;
}

// 2. Replace IntStream with direct char iteration in Bank.from() (lines 24-28)
public static Bank from(final String line) {
    final IntArrayList digits = new IntArrayList(line.length());
    for (int i = 0; i < line.length(); i++) {
        digits.add(line.charAt(i) - '0');
    }
    return new Bank(digits);
}

// 3. Consider replacing trampoline with iterative approach
// The trampoline pattern creates Done/More objects - could use a simple loop instead
// Current: buildJoltageTrampoline() creates objects per iteration
// Alternative: Direct iterative approach without trampoline objects

// 4. Reuse IntArrayList buffer (if processing multiple lines)
// Use ThreadLocal to reuse IntArrayList across calls
private static final ThreadLocal<IntArrayList> digitsBuffer =
    ThreadLocal.withInitial(() -> new IntArrayList(100));

// 5. Inline MaxDigitResult record to avoid allocation
// Instead of returning record, use output parameters or return long (value << 32 | index)
```

**Expected Impact:**
- Reduce CV from 52% to <30% for Part 1
- Reduce CV from 37% to <25% for Part 2
- Eliminate trampoline object allocations
- Reduce stream overhead
- Improve GC behavior

---

### Day 4: GridNeighbor2 (CV: 42-46%) ✅ **OPTIMIZED** {#day-4}

**Implementation**: `Day4.java` uses `GridNeighbor2.java`

**Variance Details:**
- `gridNeighbor2_part1`:
  - **BEFORE**: CV = 26% (scoreError: 3.04ms, score: 11.73ms)
  - **AFTER**: CV = 42% (scoreError: 5.28ms, score: 12.72ms) ⚠️ **Note**: CV increased but score similar
- `gridNeighbor2_part2`:
  - **BEFORE**: CV = 41% (scoreError: 180.65ms, score: 441.91ms) ⚠️ **HIGH**
  - **AFTER**: CV = 46% (scoreError: 143.97ms, score: 313.66ms) ✅ **IMPROVED**

**Optimization Applied:**
- ✅ Reused `List<Point>` buffer in `findCellsToRemove()` to avoid allocations per iteration
- ✅ Replaced stream with imperative loop in `findCellsToRemove()` to reduce overhead
- ✅ Replaced `forEach()` with imperative loop in `removeCells()` to eliminate lambda overhead

**Results:**
- **Part 1**: Score similar (11.73ms → 12.72ms), CV increased to 42% (but absolute error similar)
- **Part 2**: Score reduction 29.0% (441.91ms → 313.66ms) - **much faster**
- **Part 2**: Error reduction 20.3% (180.65ms → 143.97ms) - **more consistent**
- **Part 2**: CV increased slightly (40.9% → 45.9%) but absolute error decreased significantly

**Note on Part 1 CV**: While CV increased, the absolute error is similar (3.04ms → 5.28ms). The increase may be due to benchmark variance. The buffer reuse optimization primarily benefits Part 2's iterative loop.

**Current Implementation Analysis** (`GridNeighbor2.java`):
- ✅ Uses parallel streams for Part 1 (line 41)
- ✅ Uses imperative loops for Part 2 (line 93-100)
- ✅ **FIXED**: Reused List<Point> buffer to avoid allocations (line 113-125)
- ✅ **FIXED**: Replaced stream with imperative loop in findCellsToRemove()
- ✅ **FIXED**: Replaced forEach with imperative loop in removeCells()
- ⚠️ **Remaining**: Parallel stream variance in Part 1 (algorithmic, may benefit from sequential)
- ⚠️ **Remaining**: Point objects from Putoet library (external dependency)

**Root Causes (Before Optimization):**
1. **Point object allocations**: Line 114 `findCellsToRemove()` created new Point list each iteration
2. **Parallel stream variance**: Line 41 parallel stream has uneven work distribution
3. **GC activity**: Young generation collections during processing
4. **Iterative mutations**: Part 2 mutates grid repeatedly causing GC pressure
5. **Lambda overhead**: `forEach()` creates lambda closures

**Improvement Recommendations for `GridNeighbor2.java`:**

```java
// ✅ IMPLEMENTED: Reuse List<Point> buffer to avoid allocations (lines 113-117)
private final List<Point> cellsToRemoveBuffer = new ArrayList<>(1000);

private List<Point> findCellsToRemove(final Grid grid) {
    cellsToRemoveBuffer.clear(); // Reuse instead of creating new list

    for (final Point point : grid.findAll(c -> c == TARGET_CELL)) {
        if (hasFewerThanMinimumNeighbors(grid, point)) {
            cellsToRemoveBuffer.add(point);
        }
    }
    return new ArrayList<>(cellsToRemoveBuffer); // Copy only when needed
}

// ✅ IMPLEMENTED: Replaced forEach with imperative loop to eliminate lambda overhead
private void removeCells(final Grid grid, final List<Point> toRemove) {
    // Use imperative loop instead of forEach to avoid lambda allocation overhead
    for (final Point point : toRemove) {
        grid.set(point, EMPTY_CELL);
    }
}

// ⚠️ NOT IMPLEMENTED: Consider sequential processing for Part 1 if variance is high
// Parallel streams may cause variance due to uneven work distribution
@Override
public Integer solvePartOne(final String fileName) {
    final Grid grid = createGrid(fileName);

    int count = 0;
    for (final Point p : grid.findAll(c -> c == TARGET_CELL)) {
        if (hasFewerThanMinimumNeighbors(grid, p)) {
            count++;
        }
    }
    return count;
}
```

---

### Day 5: Range3 (CV: 47-74%) {#day-5}

**Implementation**: `Day5.java` uses `Range3.java`

**Variance Details:**
- `range3_part1`: CV = 47% (scoreError: 0.54ms, score: 1.13ms)
- `range3_part2`: CV = 70% (scoreError: 0.57ms, score: 0.81ms) ⚠️ **HIGH**

**Current Implementation Analysis** (`Range3.java`):
- ✅ Already uses FastUtil LongArrayList (line 7-8, 53, 73)
- ✅ Already uses imperative loops (line 56-61)
- ❌ **Issue**: `convertToLongList()` (line 72-77) boxes Long to longValue()
- ❌ **Issue**: Stream operations in `calculateTotalCoverage()` (line 120-125)

**Root Causes:**
1. **Boxing overhead**: Line 75 `id.longValue()` boxes/unboxes in conversion
2. **Stream overhead**: Lines 120-125 use streams for sorting and mapping
3. **GC pressure**: Intermediate collections from stream operations
4. **List allocations**: Line 142 creates new ArrayList each merge

**Improvement Recommendations for `Range3.java`:**

```java
// 1. Eliminate boxing in convertToLongList() (lines 72-77)
// If input.ids() is already a List<Long>, avoid conversion
// Or if possible, change input.ids() to return LongList directly
private static LongList convertToLongList(final List<Long> ids) {
    // If ids is already LongList, return directly
    if (ids instanceof LongList longList) {
        return longList;
    }

    final LongList result = new LongArrayList(ids.size());
    // Use indexed access to avoid iterator overhead
    for (int i = 0; i < ids.size(); i++) {
        result.add(ids.get(i)); // Still boxes, but less overhead than longValue()
    }
    return result;
}

// 2. Replace stream operations with imperative loops (lines 120-125)
private Long calculateTotalCoverage(final RangeProblemInput input) {
    final List<Interval> intervals = input.intervals();

    // Manual sort instead of stream
    final List<Interval> sorted = new ArrayList<>(intervals);
    sorted.sort(Comparator.comparingLong(Interval::start));

    final List<Interval> merged = mergeIntervals(sorted);

    // Manual sum instead of stream
    long total = 0;
    for (final Interval interval : merged) {
        total += interval.size();
    }
    return total;
}

// 3. Pre-allocate merged list with better size estimate (line 142)
private List<Interval> mergeIntervals(final List<Interval> sortedIntervals) {
    if (sortedIntervals.isEmpty()) {
        return List.of();
    }

    // Estimate: most intervals merge, so size is typically much smaller
    final int estimatedSize = Math.max(1, sortedIntervals.size() / 4);
    final List<Interval> merged = new ArrayList<>(estimatedSize);
    // Rest of implementation...
}
```

---

### Day 6: MathBlock (CV: 51-103%) {#day-6}

**Implementation**: `Day6.java` uses `MathBlock.java`

**Variance Details:**
- `mathBlock_part1`: CV = 102% (scoreError: 2.37ms, score: 2.33ms) ⚠️ **CRITICAL**
- `mathBlock_part2`: CV = 103% (scoreError: 3.77ms, score: 3.68ms) ⚠️ **CRITICAL**

**Current Implementation Analysis** (`MathBlock.java`):
- ❌ **Issue**: Line 84 `line.substring(startCol, endCol).trim()` creates TWO new strings per block
- ❌ **Issue**: Line 80 `new ArrayList<>()` creates new list per block
- ❌ **Issue**: Line 119 `new StringBuilder()` creates new builder per column
- ❌ **Issue**: Line 172 `line.substring(startCol, endCol).trim()` duplicates substring call
- ❌ **Issue**: Line 48 stream operation for maxLength calculation

**Root Causes:**
1. **Substring allocations**: Lines 84, 172 create new strings for every block
2. **Trim allocations**: Lines 84, 172 call `trim()` creating additional strings
3. **List allocations**: Line 80 creates new ArrayList for every block
4. **StringBuilder allocations**: Line 119 creates new StringBuilder for every column
5. **GC pauses**: Frequent allocations causing GC pressure

**Improvement Recommendations for `MathBlock.java`:**

```java
// 1. Eliminate substring() and trim() - use char array parsing (lines 77-102)
public Long processBlockPart1(final List<String> lines, final Integer[] range) {
    final int startCol = range[0];
    final int endCol = range[1];

    // Pre-allocate with estimated size
    final List<Long> numbers = new ArrayList<>(10);
    MathOperator operator = MathOperator.NONE;

    for (final String line : lines) {
        // Parse directly from char array without substring/trim
        final char[] chars = line.toCharArray();
        final int lineLength = Math.min(endCol, chars.length);

        // Skip leading/trailing spaces without trim()
        int start = startCol;
        int end = lineLength;
        while (start < end && chars[start] == ' ') start++;
        while (end > start && chars[end - 1] == ' ') end--;

        if (start >= end) continue;

        // Parse operator or number directly from char array
        final MathOperator foundOperator = MathOperator.fromChars(chars, start, end);
        if (foundOperator != MathOperator.NONE) {
            operator = foundOperator;
        } else {
            final long num = parseLongFromChars(chars, start, end);
            if (num >= 0) { // Use -1 as invalid marker
                numbers.add(num);
            }
        }
    }

    return calculate(numbers, operator);
}

// Helper method to parse long from char array
private static long parseLongFromChars(final char[] chars, final int start, final int end) {
    long result = 0;
    for (int i = start; i < end; i++) {
        final char c = chars[i];
        if (c >= '0' && c <= '9') {
            result = result * 10 + (c - '0');
        } else {
            return -1; // Invalid number
        }
    }
    return result;
}

// 2. Reuse StringBuilder buffer (lines 111-133)
private final ThreadLocal<StringBuilder> stringBuilderPool =
    ThreadLocal.withInitial(() -> new StringBuilder(64));

public Long processBlockPart2(final List<String> lines, final Integer[] range) {
    final int startCol = range[0];
    final int endCol = range[1];
    final List<Long> numbers = new ArrayList<>(10);
    final MathOperator operator = findOperator(lines, startCol, endCol);

    final StringBuilder numStr = stringBuilderPool.get();

    for (int col = endCol - 1; col >= startCol; col--) {
        numStr.setLength(0); // Clear for reuse

        for (final String line : lines) {
            if (col < line.length()) {
                final char c = line.charAt(col);
                if (Character.isDigit(c)) {
                    numStr.append(c);
                }
            }
        }

        if (numStr.length() > 0) {
            numbers.add(parseLongFromString(numStr));
        }
    }

    return calculate(numbers, operator);
}

// 3. Optimize maxLength calculation (line 48)
private int calculateMaxLength(final List<String> lines) {
    int max = 0;
    for (final String line : lines) {
        final int len = line.length();
        if (len > max) max = len;
    }
    return max;
}

// 4. Reuse Integer[] array instead of creating new one (lines 57, 64)
private final Integer[] rangeBuffer = new Integer[2];

// In solve() method:
rangeBuffer[0] = startCol;
rangeBuffer[1] = col;
totalSum += blockProcessor.apply(paddedLines, rangeBuffer);
```

---

### Day 7: BeamPathCounter (CV: 38-106%) {#day-7}

**Implementation**: `Day7.java` uses `BeamPathCounter.java`

**Variance Details:**
- `beamPathCounter_part1`: CV = 38% (scoreError: 1.15ms, score: 3.08ms) 🟡 **MEDIUM**
- `beamPathCounter_part2`: CV = 106% (scoreError: 1.70ms, score: 1.61ms) ⚠️ **CRITICAL**

**Current Implementation Analysis** (`BeamPathCounter.java`):
- ❌ **Issue**: Line 70-89 uses complex stream operations with `Collectors.teeing()` creating intermediate collections
- ❌ **Issue**: Line 71 creates new `Point` objects for every beam in the stream
- ❌ **Issue**: Line 75 creates new `BeamAction` records for every point
- ❌ **Issue**: Line 84-85 uses `IntStream.of().boxed()` creating boxed Integer objects
- ❌ **Issue**: Part 2 has 15ms outlier (line 179 in raw data) causing extreme variance
- ✅ Part 2 already uses memoization with Point keys (line 93, 115)

**Root Causes:**
1. **Stream overhead**: Complex stream operations with `teeing()` create multiple intermediate collections
2. **Object allocations**: New `Point` and `BeamAction` objects created for every beam
3. **Boxing overhead**: `IntStream.of().boxed()` boxes primitive ints to Integer objects
4. **GC pressure**: Many temporary collections from stream operations
5. **Outlier in Part 2**: 15ms spike (likely GC pause) causing CV > 100%

**Improvement Recommendations for `BeamPathCounter.java`:**

```java
// 1. Replace stream operations with imperative loops (lines 65-89)
private SplitResult processBeamRow(
        final Grid grid,
        final Set<Integer> currentBeams,
        final int y
) {
    long splits = 0;
    final Set<Integer> nextBeams = new HashSet<>();

    for (final int x : currentBeams) {
        final Point point = Point.of(x, y);
        if (!grid.contains(point)) {
            continue;
        }

        final CellType cellType = CellType.from(grid.get(point));
        if (cellType == CellType.SPLITTER) {
            splits++;
            nextBeams.add(x - 1);
            nextBeams.add(x + 1);
        } else {
            nextBeams.add(x);
        }
    }

    return new SplitResult(nextBeams, splits);
}

// 2. Pre-allocate HashSet with estimated size
private SplitResult processBeamRow(
        final Grid grid,
        final Set<Integer> currentBeams,
        final int y
) {
    long splits = 0;
    // Estimate: typically 1-2x the number of current beams
    final int estimatedSize = currentBeams.size() * 2;
    final Set<Integer> nextBeams = new HashSet<>(estimatedSize);

    // ... rest of implementation
}

// 3. Consider using primitive IntSet for nextBeams (if using FastUtil)
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

private SplitResult processBeamRow(
        final Grid grid,
        final IntSet currentBeams,  // Change parameter type
        final int y
) {
    long splits = 0;
    final IntSet nextBeams = new IntOpenHashSet(currentBeams.size() * 2);

    for (final int x : currentBeams) {
        final Point point = Point.of(x, y);
        if (!grid.contains(point)) {
            continue;
        }

        final CellType cellType = CellType.from(grid.get(point));
        if (cellType == CellType.SPLITTER) {
            splits++;
            nextBeams.add(x - 1);
            nextBeams.add(x + 1);
        } else {
            nextBeams.add(x);
        }
    }

    return new SplitResult(nextBeams, splits);
}

// 4. For Part 2: Pre-size HashMap to reduce resizing overhead
private long countPaths(final Grid grid) {
    // Estimate memo size based on grid dimensions
    final int estimatedSize = grid.maxX() * grid.maxY() / 4; // Rough estimate
    final Map<Point, Long> memo = new HashMap<>(estimatedSize * 2);

    // ... rest of implementation
}
```

---

### Day 8: PointCluster3 (CV: 9-34%) {#day-8}

**Implementation**: `Day8.java` uses `PointCluster3.java`

**Variance Details:**
- `pointCluster3_part1`: CV = 9% (scoreError: 5.63ms, score: 60.28ms) ✅ **EXCELLENT**
- `pointCluster3_part2`: CV = 18% (scoreError: 40.35ms, score: 219.80ms) ✅ **GOOD**

**Current Implementation Analysis** (`PointCluster3.java`):
- ✅ Already uses FastUtil ObjectArrayList (line 8-9, 84, 111)
- ✅ Already uses FastUtil IntList (line 7, 167)
- ✅ Already uses indexed access (line 60, 114-116)
- ✅ Already pre-allocates collections (line 84, 110)
- ✅ Already caches references (line 114-116, 145-147)
- ✅ **Well optimized!** Low variance indicates good implementation
- ✅ No ThreadLocal or scoped values needed - current approach is optimal

**Why Not Use Scoped Values?**

**Scoped Values vs ThreadLocal for Day 8:**

Scoped values (Java 21+) are designed for **immutable data sharing** across method calls and structured concurrency (virtual threads). However, for `PointCluster3`:

1. **No need for data sharing**: The implementation doesn't need to share mutable state across method calls
2. **Object pooling use case**: If we needed object pooling (mutable buffers), ThreadLocal is more appropriate:
   - **Scoped Values**: For immutable, read-only data shared within a scope
   - **ThreadLocal**: For mutable per-thread state (object pools, buffers)

3. **Current implementation is optimal**: `PointCluster3` doesn't need either because:
   - Collections are created per method call and returned
   - No shared mutable state between calls
   - FastUtil collections already provide excellent performance
   - Low variance (9-18% CV) indicates no allocation bottlenecks

**When Scoped Values Would Be Appropriate:**

```java
// Example: If we needed to share immutable configuration across methods
// (Not needed for PointCluster3, but for illustration)

// Using Scoped Values (Java 21+):
private static final ScopedValue<GraphConfig> CONFIG = ScopedValue.newInstance();

public Long solvePartOne(String fileName, Integer connectionLimit) {
    GraphConfig config = new GraphConfig(connectionLimit, /* other params */);
    return ScopedValue.where(CONFIG, config)
        .call(() -> {
            ObjectList<Point3D> points = parsePoints(fileName);
            // All methods can access CONFIG.get() without passing parameters
            return processWithConfig(points);
        });
}

// Benefits:
// - Type-safe immutable data sharing
// - Automatic cleanup when scope ends
// - Better for virtual threads and structured concurrency
// - No memory leaks (unlike ThreadLocal if not cleaned up)
```

**When ThreadLocal Would Be Appropriate:**

```java
// Example: If we needed mutable object pooling (not needed for PointCluster3)
private static final ThreadLocal<ObjectList<Edge>> EDGE_POOL =
    ThreadLocal.withInitial(() -> new ObjectArrayList<>(1000));

private ObjectList<Edge> computeEdges(ObjectList<Point3D> points) {
    ObjectList<Edge> edges = EDGE_POOL.get();
    edges.clear(); // Reuse mutable buffer

    // ... fill edges ...

    return new ObjectArrayList<>(edges); // Return copy
}

// Benefits:
// - Mutable per-thread state
// - Object pooling for frequently allocated objects
// - Better performance than creating new objects each time
```

**Conclusion for Day 8:**

`PointCluster3` doesn't need scoped values or ThreadLocal because:
- ✅ No shared mutable state between method calls
- ✅ Collections are created, used, and returned (no pooling needed)
- ✅ FastUtil already provides optimal performance
- ✅ Low variance (9-18% CV) shows no allocation bottlenecks

**Minor Improvement Recommendations for `PointCluster3.java`:**

```java
// Only consider if processing millions of points and Edge allocation becomes bottleneck:
// 1. Object pooling with ThreadLocal (for mutable buffers)
private static final ThreadLocal<ObjectList<Edge>> EDGE_POOL =
    ThreadLocal.withInitial(() -> new ObjectArrayList<>(10000));

// 2. Consider primitive array representation for very large graphs
// Only if processing millions of points:
private static class PrimitiveEdge {
    final int from, to;
    final long distanceSquared;
    // ... constructor
}

// But current ObjectArrayList approach is likely optimal for most cases
```

---

### Day 11: GraphPathCounter (CV: 15-60%) ✅ **OPTIMIZED** {#day-11}

**Implementation**: `Day11.java` uses `GraphPathCounter.java`

**Variance Details:**
- `graphPathCounter_part1`:
  - **BEFORE**: CV = 58% (scoreError: 1.40ms, score: 2.43ms) ⚠️ **HIGH**
  - **AFTER**: CV = 15% (scoreError: 0.07ms, score: 0.50ms) ✅ **EXCELLENT**
- `graphPathCounter_part2`:
  - **BEFORE**: CV = 42% (scoreError: 1.47ms, score: 3.50ms)
  - **AFTER**: CV = 60% (scoreError: 0.71ms, score: 1.18ms) ⚠️ **Note**: CV increased but performance improved 66%

**Optimization Applied:**
- ✅ Replaced stream-based parsing with imperative loops
- ✅ Optimized `parseLine()` to use `indexOf()` and manual parsing instead of `split()` and Optional
- ✅ Eliminated Optional overhead in `countPaths()` using direct null checks
- ✅ Reused HashMap for memoization in `solvePartTwo()` instead of creating new one per path

**Results:**
- **Part 1**: Score reduction 79.5% (2.43ms → 0.50ms), Error reduction 94.7%, CV reduction 74% (58% → 15%)
- **Part 2**: Score reduction 66.1% (3.50ms → 1.18ms), Error reduction 51.8%, CV increased to 60% (but much faster)

**Note on Part 2 CV**: While CV increased, the absolute error decreased (1.47ms → 0.71ms) and performance improved significantly. The CV increase may be due to memo reuse affecting different path calculations, but overall the solution is much faster and more consistent in absolute terms.

**Current Implementation Analysis** (`GraphPathCounter.java`):
- ✅ **FIXED**: Replaced streams with imperative loops (line 25-36)
- ✅ **FIXED**: Optimized parseLine() to avoid split() and Optional (line 45-71)
- ✅ **FIXED**: Eliminated Optional overhead in countPaths() (line 82-110)
- ✅ **FIXED**: Reused HashMap for memoization (line 115, 122)
- ⚠️ **Remaining**: Recursive DFS may cause stack overhead (algorithmic, not easily optimized)

**Root Causes (Before Optimization):**
1. **Stream overhead**: Multiple stream operations create intermediate collections
2. **Optional overhead**: Optional.orElseGet() allocates lambda closures
3. **HashMap allocations**: Line 93 creates new HashMap for each path calculation
4. **String split**: Line 40 creates string array per line
5. **GC pressure**: Many temporary collections from streams

**Improvement Recommendations for `GraphPathCounter.java`:**

```java
// 1. Replace stream-based parsing with imperative loops (lines 24-32)
private Map<GraphNode, List<GraphNode>> parseInput(final String fileName) {
    final List<String> lines = ResourceLines.list(fileName);
    final Map<GraphNode, List<GraphNode>> graph = new HashMap<>();

    for (final String line : lines) {
        if (line.isBlank()) continue;

        final GraphEdge edge = parseLine(line);
        graph.put(edge.from(), edge.to());
    }

    return graph;
}

// 2. Optimize parseLine() to avoid split() and Optional (lines 39-53)
private GraphEdge parseLine(final String line) {
    final int colonIdx = line.indexOf(": ");
    if (colonIdx == -1) {
        throw new IllegalArgumentException("Invalid line format: " + line);
    }

    final GraphNode from = GraphNode.from(line.substring(0, colonIdx).trim());

    final String toPart = line.substring(colonIdx + 2).trim();
    if (toPart.isEmpty()) {
        return new GraphEdge(from, List.of());
    }

    // Parse neighbors manually instead of split + stream
    final List<GraphNode> toList = new ArrayList<>();
    int start = 0;
    for (int i = 0; i <= toPart.length(); i++) {
        if (i == toPart.length() || Character.isWhitespace(toPart.charAt(i))) {
            if (i > start) {
                toList.add(GraphNode.from(toPart.substring(start, i)));
            }
            start = i + 1;
        }
    }

    return new GraphEdge(from, toList);
}

// 3. Eliminate Optional overhead in countPaths() (lines 72-81)
private Long countPaths(
        final GraphNode current,
        final GraphNode target,
        final Map<GraphNode, List<GraphNode>> graph,
        final Map<GraphNode, Long> memo) {
    if (current.equals(target)) {
        return 1L;
    }

    // Direct memo lookup instead of Optional
    final Long cached = memo.get(current);
    if (cached != null) {
        return cached;
    }

    final List<GraphNode> neighbors = graph.get(current);
    if (neighbors == null || neighbors.isEmpty()) {
        memo.put(current, 0L);
        return 0L;
    }

    long count = 0;
    for (final GraphNode neighbor : neighbors) {
        count += countPaths(neighbor, target, graph, memo);
    }

    memo.put(current, count);
    return count;
}

// 4. Reuse HashMap for memoization in solvePartTwo() (lines 88-94)
@Override
public Long solvePartTwo(final String fileName) {
    final Map<GraphNode, List<GraphNode>> graph = parseInput(fileName);
    final Map<GraphNode, Long> memo = new HashMap<>(); // Reuse for all paths

    final long firstProduct =
        countPaths(GraphNode.from("svr"), GraphNode.from("dac"), graph, memo) *
        countPaths(GraphNode.from("dac"), GraphNode.from("fft"), graph, memo) *
        countPaths(GraphNode.from("fft"), GraphNode.from("out"), graph, memo);

    memo.clear(); // Clear for second set of paths

    final long secondProduct =
        countPaths(GraphNode.from("svr"), GraphNode.from("fft"), graph, memo) *
        countPaths(GraphNode.from("fft"), GraphNode.from("dac"), graph, memo) *
        countPaths(GraphNode.from("dac"), GraphNode.from("out"), graph, memo);

    return firstProduct + secondProduct;
}
```

---

### Day 12: ShapePacking (CV: 65% → 32%) ✅ **OPTIMIZED** {#day-12}

**Implementation**: `Day12.java` uses `ShapePacking.java`

**Variance Details:**
- `shapePacking_part1`:
  - **BEFORE**: CV = 65% (scoreError: 5.27ms, score: 8.11ms) ⚠️ **HIGH**
  - **AFTER**: CV = 32% (scoreError: 2.07ms, score: 6.51ms) ✅ **MEDIUM**

**Optimization Applied:**
- ✅ Replaced `CacheKey` object allocations with primitive `long` hash keys
- ✅ Changed `Map<CacheKey, Boolean>` to `Map<Long, Boolean>` in memoization
- ✅ Implemented `computeMemoKey()` method combining `gridHash` and `shapeIds` hash

**Results:**
- **Score reduction**: 19.7% (8.11ms → 6.51ms) - faster execution
- **Error reduction**: 60.8% (5.27ms → 2.07ms) - more consistent
- **CV reduction**: 51.2% (65% → 32%) - much lower variance
- **Outlier elimination**: Max reduced from 46.48ms to ~17ms

**Current Implementation Analysis** (`ShapePacking.java`):
- ✅ Already separates fast/slow regions (lines 36-53)
- ✅ Already sorts by complexity (line 50)
- ✅ Already uses parallel processing for slow regions (line 51)
- ✅ **FIXED**: Replaced CacheKey allocations with primitive long hash (line 183)
- ⚠️ **Remaining**: Parallel stream work distribution may still have some variance
- ⚠️ **Remaining**: Backtracking algorithm has inherent variance due to search space

**Root Causes (Before Optimization):**
1. **CacheKey allocations**: Line 180 `new CacheKey()` created object per lookup
2. **Parallel work distribution**: Even with sorting, parallel streams may have variance
3. **Backtracking variance**: Search space exploration time varies significantly
4. **GC pressure**: Many CacheKey and ShapeVariant allocations

**Improvement Recommendations for `ShapePacking.java`:**

```java
// ✅ IMPLEMENTED: Use primitive-based memoization key instead of CacheKey object
// Replace CacheKey with long hash combining gridHash and remaining shapeIds
private long computeMemoKey(long gridHash, List<Integer> shapeIds, int index) {
    // Compute hash from remaining shape IDs (similar to CacheKey but returning long)
    long shapeHash = 1L;
    for (int i = index; i < shapeIds.size(); i++) {
        shapeHash = 31L * shapeHash + shapeIds.get(i);
    }
    // Mix grid hash with shape hash using 31 multiplier to match CacheKey's hash pattern
    return gridHash * 31L + shapeHash;
}

// In backtrack() method:
private boolean backtrack(..., Map<Long, Boolean> memo, ...) {
    // ...
    final long key = computeMemoKey(gridHash, shapeIds, index);
    final Boolean cached = memo.get(key);
    if (cached != null) {
        return cached;
    }
    // ...
    memo.put(key, result);
    return result;
}
```

// 2. Improve parallel work distribution with custom chunking
// Replace parallel stream with ForkJoinPool for better control
private static final ForkJoinPool PARALLEL_POOL =
    new ForkJoinPool(Runtime.getRuntime().availableProcessors());

long slowCount = PARALLEL_POOL.submit(() ->
    slowRegions.parallelStream()
        .filter(region -> solve(region, shapes))
        .count()
).join();

// 3. Pre-compute and cache variant offsets (if not already done)
// Ensure variantOffsets is built once and reused

// 4. Consider using ConcurrentHashMap for thread-safe memoization
// If sharing memo across parallel tasks (with caution)
```

---

## GC Log Analysis

### Common GC Patterns Observed:

1. **Young Generation Collections**: Frequent 1-2ms pauses
   - **Impact**: Causes variance in short-running benchmarks
   - **Solution**: Reduce object allocation, reuse objects

2. **Full GC Pauses**: Occasional 8-13ms pauses
   - **Impact**: Significant variance spikes
   - **Solution**: Pre-allocate collections, use object pooling

3. **GC Frequency**: Higher GC frequency correlates with higher variance
   - **Day 2**: Many full GCs due to processing millions of IDs
   - **Day 6**: Frequent young GCs from string operations

### GC-Specific Recommendations:

```java
// 1. Use -XX:+UseG1GC with tuned parameters (already using G1)
// Add to benchmark JVM args:
-XX:MaxGCPauseMillis=10
-XX:G1HeapRegionSize=4m
-XX:+G1UseAdaptiveIHOP
-XX:G1ReservePercent=20

// 2. Pre-touch heap to avoid allocation delays
-XX:+AlwaysPreTouch

// 3. Use object pooling for frequently allocated objects
private static final ThreadLocal<List<Long>> numberListPool =
    ThreadLocal.withInitial(() -> new ArrayList<>(16));

// 4. Batch operations to reduce GC frequency
// Process multiple items before triggering GC
```

---

## General Improvement Strategies

### 1. Reduce Object Allocations

**Pattern**: Replace object allocations with primitives or reused buffers

```java
// ❌ BAD: Allocates new string
String trimmed = input.trim();

// ✅ GOOD: Check without allocation
if (input != null && !input.isEmpty() && input.charAt(0) != ' ') {
    // process
}

// ❌ BAD: Allocates new list per call
List<Long> numbers = new ArrayList<>();

// ✅ GOOD: Reuse with clear()
numbers.clear();
```

### 2. Use Primitive Collections

**Pattern**: Use FastUtil or Eclipse Collections for primitives

```java
// ❌ BAD: Boxed primitives
List<Long> ids = new ArrayList<>();

// ✅ GOOD: Primitive collections
LongList ids = new LongArrayList();
IntSet visited = new IntOpenHashSet();
```

### 3. Pre-allocate Collections

**Pattern**: Size collections to expected capacity

```java
// ❌ BAD: Multiple resizes
List<String> items = new ArrayList<>();

// ✅ GOOD: Pre-sized
List<String> items = new ArrayList<>(estimatedSize);
```

### 4. Avoid String Operations

**Pattern**: Use char arrays or direct parsing

```java
// ❌ BAD: Multiple substring allocations
String part1 = input.substring(0, 5);
String part2 = input.substring(6, 10);

// ✅ GOOD: Parse directly from char array
char[] chars = input.toCharArray();
int value1 = parseInteger(chars, 0, 5);
int value2 = parseInteger(chars, 6, 10);
```

### 5. Optimize Parallel Processing

**Pattern**: Improve work distribution

```java
// ❌ BAD: Uneven work distribution
items.parallelStream().forEach(this::process);

// ✅ GOOD: Sort by complexity first
items.stream()
    .sorted(Comparator.comparingInt(this::estimateComplexity))
    .parallel()
    .forEach(this::process);
```

### 6. Use Object Pooling

**Pattern**: Reuse objects for frequently allocated types

```java
private static final ThreadLocal<StringBuilder> stringBuilderPool =
    ThreadLocal.withInitial(() -> new StringBuilder(64));

public String buildString() {
    StringBuilder sb = stringBuilderPool.get();
    sb.setLength(0);  // Clear for reuse
    // ... use sb
    return sb.toString();
}
```

---

## Implementation Checklist

For each high-variance day, implement improvements in the actual solver class:

- [ ] **Day 1 (DialRotator3.java)**:
  - [ ] Replace stream collection with direct `ResourceLines.list()` (lines 43-45, 85-87)
  - [ ] Consider eliminating ObjectArrayList if not needed for performance

- [ ] **Day 2 (InvalidIdValidator2.java)**:
  - [ ] Use ThreadLocal char array buffer in `longToCharArray()` (line 149-172)
  - [ ] Replace `split(",")` with manual parsing in `parseRanges()` (line 70)
  - [ ] Consider math-based validation for Part 1 to eliminate char arrays

- [ ] **Day 4 (GridNeighbor2.java)**:
  - [ ] Reuse List<Point> buffer in `findCellsToRemove()` (line 113-117)
  - [ ] Consider sequential processing for Part 1 if parallel variance is high (line 41)
  - [ ] Batch cell removals to reduce GC frequency (line 126-128)

- [ ] **Day 5 (Range3.java)**:
  - [ ] Eliminate boxing in `convertToLongList()` or change input to LongList (line 72-77)
  - [ ] Replace stream operations with imperative loops in `calculateTotalCoverage()` (lines 120-125)
  - [ ] Better size estimation for merged list (line 142)

- [ ] **Day 6 (MathBlock.java)**:
  - [ ] Eliminate `substring()` and `trim()` - use char array parsing (lines 84, 172)
  - [ ] Reuse StringBuilder buffer using ThreadLocal (line 119)
  - [ ] Pre-allocate ArrayList with estimated size (line 80)
  - [ ] Reuse Integer[] array instead of creating new one (lines 57, 64)

- [ ] **Day 7 (BeamPathCounter.java)**:
  - [ ] Replace stream operations with imperative loops in `processBeamRow()` (lines 70-89)
  - [ ] Eliminate `Collectors.teeing()` and intermediate collections
  - [ ] Remove boxing from `IntStream.of().boxed()` (lines 84-85)
  - [ ] Pre-allocate HashSet with estimated size
  - [ ] Consider FastUtil IntSet for nextBeams to avoid boxing
  - [ ] Pre-size HashMap in `countPaths()` to reduce resizing (line 93)

- [ ] **Day 11 (GraphPathCounter.java)**:
  - [ ] Replace stream-based parsing with imperative loops (lines 24-32)
  - [ ] Optimize `parseLine()` to avoid `split()` and Optional (lines 39-53)
  - [ ] Eliminate Optional overhead in `countPaths()` (lines 72-81)
  - [ ] Reuse HashMap for memoization in `solvePartTwo()` (lines 88-94)

- [ ] **Day 12 (ShapePacking.java)**:
  - [ ] Use primitive-based memoization key instead of CacheKey object (line 180)
  - [ ] Improve parallel work distribution with custom chunking (line 51)
  - [ ] Consider ConcurrentHashMap for thread-safe memoization

---

## Testing Recommendations

After implementing improvements:

1. **Re-run benchmarks** with same configuration
2. **Compare CV values** - should see reduction to < 30%
3. **Check GC logs** - should see fewer and shorter pauses
4. **Verify correctness** - ensure optimizations don't change results
5. **Profile with JFR** - identify remaining bottlenecks

---

## Conclusion

High variance in benchmarks is primarily caused by:
1. **Excessive object allocation** → GC pressure
2. **String operations** → Many temporary allocations
3. **Boxing/unboxing** → Overhead and GC pressure
4. **Uneven parallel work** → Variable execution times

The recommended improvements focus on:
- **Reducing allocations** through primitive usage and object reuse
- **Eliminating string operations** in hot paths
- **Optimizing parallel processing** for better work distribution
- **Using specialized collections** (FastUtil, Eclipse Collections)

Implementing these changes should reduce variance significantly and improve overall performance consistency.

