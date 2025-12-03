# Big O Notation Guide

A comprehensive guide to understanding and analyzing time and space complexity using Big O notation.

## Table of Contents

1. [Introduction](#introduction)
2. [What is Big O Notation?](#what-is-big-o-notation)
3. [Common Time Complexities](#common-time-complexities)
4. [Space Complexity](#space-complexity)
5. [Best, Average, and Worst Case](#best-average-and-worst-case)
6. [Common Patterns and Examples](#common-patterns-and-examples)
7. [Analyzing Algorithms](#analyzing-algorithms)
8. [Optimization Strategies](#optimization-strategies)
9. [Practical Tips](#practical-tips)
10. [References](#references)

## Introduction

Big O notation is a mathematical notation used to describe the limiting behavior of a function when the argument tends towards a particular value or infinity. In computer science, it's used to classify algorithms according to how their runtime or space requirements grow as the input size increases.

### Why Big O Matters

- **Scalability**: Understand how algorithms perform as data grows
- **Optimization**: Identify bottlenecks and optimization opportunities
- **Comparison**: Compare different algorithmic approaches
- **Decision Making**: Choose the right algorithm for your use case

## What is Big O Notation?

Big O notation describes the **upper bound** of an algorithm's complexity. It focuses on the worst-case scenario and ignores constants and lower-order terms.

### Formal Definition

For a function `f(n)`, we say `f(n) = O(g(n))` if there exist positive constants `c` and `n₀` such that:

```
f(n) ≤ c × g(n) for all n ≥ n₀
```

### Key Principles

1. **Ignore Constants**: O(2n) = O(n)
2. **Ignore Lower Order Terms**: O(n² + n) = O(n²)
3. **Focus on Dominant Term**: The term that grows fastest determines the complexity

### Example

```java
// O(n) - Linear time
for (int i = 0; i < n; i++) {
    // constant time operation
}

// O(n²) - Quadratic time
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        // constant time operation
    }
}
```

## Common Time Complexities

### O(1) - Constant Time

The algorithm takes the same amount of time regardless of input size.

**Examples:**
- Array access by index
- Hash map lookup (average case)
- Stack push/pop operations

```java
// O(1)
int getElement(int[] array, int index) {
    return array[index];
}

// O(1) - HashMap lookup (average case)
String value = map.get(key);
```

**Characteristics:**
- Fastest possible complexity
- Ideal for frequently called operations
- Independent of input size

### O(log n) - Logarithmic Time

The algorithm's runtime grows logarithmically with input size. Each operation eliminates half of the remaining possibilities.

**Examples:**
- Binary search
- Balanced binary tree operations
- Heap operations

```java
// O(log n) - Binary search
int binarySearch(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target) return mid;
        if (arr[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}
```

**Characteristics:**
- Very efficient
- Common in divide-and-conquer algorithms
- Base of logarithm doesn't matter (log₂n = O(log n))

### O(n) - Linear Time

The algorithm's runtime grows proportionally with input size.

**Examples:**
- Iterating through an array
- Finding maximum element
- Linear search

```java
// O(n) - Find maximum
int findMax(int[] array) {
    int max = array[0];
    for (int i = 1; i < array.length; i++) {
        if (array[i] > max) {
            max = array[i];
        }
    }
    return max;
}
```

**Characteristics:**
- Must examine each element at least once
- Optimal for many problems
- Common in single-pass algorithms

### O(n log n) - Linearithmic Time

Common in efficient sorting and divide-and-conquer algorithms.

**Examples:**
- Merge sort
- Heap sort
- Quick sort (average case)
- Tree operations on all nodes

```java
// O(n log n) - Merge sort
void mergeSort(int[] arr, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);      // O(log n) levels
        mergeSort(arr, mid + 1, right); // O(log n) levels
        merge(arr, left, mid, right);   // O(n) per level
    }
}
```

**Characteristics:**
- Efficient for sorting
- Lower bound for comparison-based sorting
- Common in divide-and-conquer algorithms

### O(n²) - Quadratic Time

Runtime grows quadratically with input size. Often involves nested loops.

**Examples:**
- Bubble sort
- Selection sort
- Insertion sort
- Checking all pairs in an array

```java
// O(n²) - Bubble sort
void bubbleSort(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr.length - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                swap(arr, j, j + 1);
            }
        }
    }
}
```

**Characteristics:**
- Acceptable for small inputs
- Becomes slow for large datasets
- Often indicates optimization opportunity

### O(n³) - Cubic Time

Runtime grows cubically. Usually involves three nested loops.

**Examples:**
- Matrix multiplication (naive)
- Finding all triplets
- Some graph algorithms

```java
// O(n³) - Naive matrix multiplication
int[][] multiply(int[][] A, int[][] B) {
    int n = A.length;
    int[][] C = new int[n][n];
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                C[i][j] += A[i][k] * B[k][j];
            }
        }
    }
    return C;
}
```

### O(2ⁿ) - Exponential Time

Runtime doubles with each additional input element. Very inefficient.

**Examples:**
- Recursive Fibonacci (naive)
- Generating all subsets
- Solving some NP-complete problems

```java
// O(2ⁿ) - Naive recursive Fibonacci
int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}
```

**Characteristics:**
- Extremely slow
- Often indicates need for optimization
- Can be improved with memoization or dynamic programming

### O(n!) - Factorial Time

Runtime grows factorially. Extremely inefficient.

**Examples:**
- Generating all permutations
- Traveling Salesman Problem (brute force)
- Some constraint satisfaction problems

```java
// O(n!) - Generate all permutations
void permute(int[] arr, int start) {
    if (start == arr.length - 1) {
        // process permutation
        return;
    }
    for (int i = start; i < arr.length; i++) {
        swap(arr, start, i);
        permute(arr, start + 1);
        swap(arr, start, i); // backtrack
    }
}
```

## Space Complexity

Space complexity measures the amount of memory an algorithm uses relative to input size.

### Common Space Complexities

- **O(1)**: Constant space - fixed amount of memory
- **O(log n)**: Logarithmic space - recursive calls with log depth
- **O(n)**: Linear space - proportional to input size
- **O(n log n)**: Linearithmic space
- **O(n²)**: Quadratic space - 2D arrays, adjacency matrices

### Examples

```java
// O(1) space - In-place sorting
void bubbleSort(int[] arr) {
    // Only uses a few variables
}

// O(n) space - Creating a copy
int[] copyArray(int[] arr) {
    int[] copy = new int[arr.length];
    // ...
    return copy;
}

// O(log n) space - Recursive binary search
int binarySearch(int[] arr, int target, int left, int right) {
    // Recursion depth is O(log n)
}
```

## Best, Average, and Worst Case

### Best Case (Ω - Omega)

The minimum time/space required for any input of size n.

**Example:** Linear search best case is O(1) when the element is at the first position.

### Average Case (Θ - Theta)

The expected time/space for a typical input.

**Example:** Quick sort average case is O(n log n), but worst case is O(n²).

### Worst Case (O - Big O)

The maximum time/space required for any input of size n.

**Example:** Linear search worst case is O(n) when the element is at the last position or not present.

### Why It Matters

- **Worst Case**: Guarantees performance upper bound
- **Average Case**: More realistic for typical usage
- **Best Case**: Rarely useful, often misleading

## Common Patterns and Examples

### Pattern 1: Single Loop

```java
// O(n) - Linear time
for (int i = 0; i < n; i++) {
    // constant time operations
}
```

### Pattern 2: Nested Loops

```java
// O(n²) - Quadratic time
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        // constant time operations
    }
}

// O(n × m) - Different sizes
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        // constant time operations
    }
}
```

### Pattern 3: Divide and Conquer

```java
// O(n log n) - Merge sort
void divideAndConquer(int[] arr, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        divideAndConquer(arr, left, mid);      // T(n/2)
        divideAndConquer(arr, mid + 1, right); // T(n/2)
        merge(arr, left, mid, right);          // O(n)
    }
}
// T(n) = 2T(n/2) + O(n) = O(n log n)
```

### Pattern 4: Recursion with Memoization

```java
// O(n) with memoization vs O(2ⁿ) without
Map<Integer, Integer> memo = new HashMap<>();

int fibonacci(int n) {
    if (memo.containsKey(n)) return memo.get(n);
    if (n <= 1) return n;
    int result = fibonacci(n - 1) + fibonacci(n - 2);
    memo.put(n, result);
    return result;
}
```

### Pattern 5: Two Pointers

```java
// O(n) - Single pass with two pointers
boolean hasPair(int[] arr, int target) {
    int left = 0, right = arr.length - 1;
    while (left < right) {
        int sum = arr[left] + arr[right];
        if (sum == target) return true;
        if (sum < target) left++;
        else right--;
    }
    return false;
}
```

## Analyzing Algorithms

### Step-by-Step Analysis

1. **Identify the input size** (n, m, V, E, etc.)
2. **Count operations** in terms of input size
3. **Identify the dominant term**
4. **Remove constants and lower-order terms**
5. **Express in Big O notation**

### Example Analysis

```java
void example(int[] arr) {
    // Step 1: Input size is arr.length = n
    
    // Step 2: Count operations
    Arrays.sort(arr);                    // O(n log n)
    
    for (int i = 0; i < arr.length; i++) { // O(n)
        System.out.println(arr[i]);        // O(1) per iteration
    }
    
    // Step 3: Dominant term is O(n log n)
    // Step 4: Already simplified
    // Step 5: O(n log n)
}
```

### Common Data Structure Operations

| Data Structure | Access | Search | Insertion | Deletion |
|---------------|--------|--------|-----------|----------|
| Array | O(1) | O(n) | O(n) | O(n) |
| Linked List | O(n) | O(n) | O(1) | O(1) |
| Hash Table | O(1)* | O(1)* | O(1)* | O(1)* |
| Binary Search Tree | O(log n)* | O(log n)* | O(log n)* | O(log n)* |
| Balanced BST | O(log n) | O(log n) | O(log n) | O(log n) |
| Heap | O(1) | O(n) | O(log n) | O(log n) |

*Average case; worst case may differ

## Optimization Strategies

### 1. Reduce Time Complexity

**Before:** O(n²) nested loops
```java
// O(n²)
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        if (arr[i] + arr[j] == target) return true;
    }
}
```

**After:** O(n) using HashMap
```java
// O(n)
Map<Integer, Integer> map = new HashMap<>();
for (int i = 0; i < n; i++) {
    int complement = target - arr[i];
    if (map.containsKey(complement)) return true;
    map.put(arr[i], i);
}
```

### 2. Use Appropriate Data Structures

- **HashSet/HashMap**: O(1) lookups instead of O(n) linear search
- **Priority Queue**: O(log n) insertions instead of O(n) sorting
- **TreeSet/TreeMap**: O(log n) operations with sorted order

### 3. Memoization

Convert exponential recursion to polynomial time:

```java
// Before: O(2ⁿ)
int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}

// After: O(n) with memoization
Map<Integer, Integer> memo = new HashMap<>();
int fibonacci(int n) {
    if (memo.containsKey(n)) return memo.get(n);
    if (n <= 1) return n;
    int result = fibonacci(n - 1) + fibonacci(n - 2);
    memo.put(n, result);
    return result;
}
```

### 4. Early Termination

```java
// Before: Always O(n)
boolean contains(int[] arr, int target) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == target) return true;
    }
    return false;
}

// After: O(1) best case, O(n) worst case
// (Complexity is still O(n) worst case, but average case improves)
```

### 5. Preprocessing

```java
// Before: O(n²) - repeated calculations
for (int i = 0; i < queries.length; i++) {
    int sum = 0;
    for (int j = queries[i].start; j <= queries[i].end; j++) {
        sum += arr[j];
    }
}

// After: O(n + q) - prefix sum preprocessing
int[] prefixSum = new int[arr.length + 1];
for (int i = 0; i < arr.length; i++) {
    prefixSum[i + 1] = prefixSum[i] + arr[i];
}
for (int i = 0; i < queries.length; i++) {
    int sum = prefixSum[queries[i].end + 1] - prefixSum[queries[i].start];
}
```

## Practical Tips

### 1. Focus on Scalability

- Consider how the algorithm performs as input grows
- O(n²) might be acceptable for n=100, but not for n=1,000,000

### 2. Measure, Don't Guess

- Profile your code to find actual bottlenecks
- Premature optimization is the root of all evil
- Use profiling tools (JProfiler, VisualVM, etc.)

### 3. Consider Constants

- O(n) might be slower than O(n log n) for small inputs due to constants
- Real-world performance depends on implementation details

### 4. Space-Time Tradeoffs

- Sometimes you can trade space for time (memoization, caching)
- Sometimes you can trade time for space (streaming, lazy evaluation)

### 5. Know Your Constraints

- Input size limits
- Time constraints
- Memory constraints
- These determine acceptable complexity

### 6. Common Pitfalls

**Pitfall 1:** Ignoring hidden complexity
```java
// Looks like O(n), but contains() is O(n)
for (int i = 0; i < list.size(); i++) {
    if (list.contains(target)) { // O(n) operation!
        // ...
    }
}
// Actual complexity: O(n²)
```

**Pitfall 2:** String concatenation in loops
```java
// O(n²) - creates new string each time
String result = "";
for (int i = 0; i < n; i++) {
    result += arr[i]; // O(n) operation
}

// O(n) - use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; i++) {
    sb.append(arr[i]); // O(1) amortized
}
```

**Pitfall 3:** Nested operations
```java
// O(n²) - nested operations
for (int i = 0; i < list.size(); i++) {
    Collections.sort(list); // O(n log n) each iteration!
}
```

## Complexity Comparison

Visual comparison of common complexities (assuming 1 operation = 1 microsecond):

| n | O(1) | O(log n) | O(n) | O(n log n) | O(n²) | O(2ⁿ) | O(n!) |
|---|------|----------|------|------------|-------|-------|-------|
| 10 | 1μs | 3μs | 10μs | 33μs | 100μs | 1ms | 3.6s |
| 100 | 1μs | 7μs | 100μs | 664μs | 10ms | 10¹³ years | - |
| 1,000 | 1μs | 10μs | 1ms | 10ms | 1s | - | - |
| 10,000 | 1μs | 13μs | 10ms | 133ms | 100s | - | - |
| 100,000 | 1μs | 17μs | 100ms | 1.7s | 2.8 hours | - | - |

## References

### Books
- "Introduction to Algorithms" by Cormen, Leiserson, Rivest, and Stein
- "Algorithm Design Manual" by Steven Skiena
- "Cracking the Coding Interview" by Gayle Laakmann McDowell

### Online Resources
- [Big-O Cheat Sheet](https://www.bigocheatsheet.com/)
- [Khan Academy - Algorithms](https://www.khanacademy.org/computing/computer-science/algorithms)
- [VisuAlgo - Algorithm Visualizations](https://visualgo.net/)

### Tools
- Profiling: JProfiler, VisualVM, Java Flight Recorder
- Complexity Analysis: IntelliJ IDEA Complexity Analysis Plugin

## Conclusion

Understanding Big O notation is essential for:
- Writing efficient code
- Making informed algorithmic choices
- Optimizing performance bottlenecks
- Communicating algorithm efficiency

Remember:
- **Worst case** complexity provides guarantees
- **Average case** is often more realistic
- **Constants matter** in practice, but not in Big O
- **Measure** before optimizing
- **Choose** the right tool for the job

---

*Last updated: 2024*
*For specific algorithm analyses, see [2024.md](./2024.md) for Advent of Code 2024 solutions.*
