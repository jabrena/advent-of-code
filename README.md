# Advent of code collection

Advent of Code is an annual set of Christmas-themed computer programming challenges that follow an Advent calendar.

[![Java CI](https://github.com/jabrena/advent-of-code/actions/workflows/maven.yml/badge.svg)](https://github.com/jabrena/advent-of-code/actions/workflows/maven.yml)

[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-light.svg)](https://sonarcloud.io/summary/new_code?id=jabrena_advent-of-code)

## Cloud IDEs

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/jabrena/advent-of-code)

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/jabrena/advent-of-code)

## Benefits solving AOC problems

- **Improve Java programming skills**: Master modern Java features (Java 25), SOLID principles, Effective Java best practices, and clean code techniques

- **Functional Programming & Design**:
  - **Sealed Interfaces & ADTs**: Type-safe algebraic data types for pattern matching (e.g., [`MathOperator`](2025/src/main/java/info/jab/aoc2025/day6/MathOperator.java) with sealed interface and record implementations)
  - **Records**: Immutable data carriers for state representation (e.g., [`Interval`](2025/src/main/java/info/jab/aoc2025/day5/Interval.java), [`RangeMergeState`](2025/src/main/java/info/jab/aoc2025/day5/RangeMergeState.java), [`ParsedData`](2025/src/main/java/info/jab/aoc2025/day12/ParsedData.java), [`Shape`](2025/src/main/java/info/jab/aoc2025/day12/Shape.java), [`Region`](2025/src/main/java/info/jab/aoc2025/day12/Region.java))
  - **Streams API**: Declarative data transformations, `Stream.iterate()` for functional iteration, lazy evaluation (e.g., [`Range2`](2025/src/main/java/info/jab/aoc2025/day5/Range2.java), [`ShapePacking`](2025/src/main/java/info/jab/aoc2025/day12/ShapePacking.java))
  - **Pattern Matching**: Switch expressions with sealed classes for exhaustive type checking
  - **Trampoline Pattern**: Converting deep recursion to iteration, preventing stack overflow
  - **Pure Functions**: Side-effect-free transformations, immutable data structures (e.g., [`Interval.contains()`](2025/src/main/java/info/jab/aoc2025/day5/Interval.java), [`Range2.mergeIntervals()`](2025/src/main/java/info/jab/aoc2025/day5/Range2.java))
  - **Higher-Order Functions**: Function composition, method references, lambda expressions
  - **Parallel Processing**: Parallel streams for independent computations (e.g., [`ShapePacking`](2025/src/main/java/info/jab/aoc2025/day12/ShapePacking.java) with `parallelStream()`)

- **Data Oriented Programming (DOP)**:
  - **Code-Data Separation**: Instructions as data structures, execution logic separated from instruction representation
  - **Generic Data Structures**: Flexible, reusable data representations (e.g., generic `Trampoline<T>`)
  - **Immutable Data**: Records and sealed interfaces ensure data integrity (e.g., [`RangeProblemInput`](2025/src/main/java/info/jab/aoc2025/day5/RangeProblemInput.java), [`GraphNode`](2025/src/main/java/info/jab/aoc2025/day11/GraphNode.java), [`GraphEdge`](2025/src/main/java/info/jab/aoc2025/day11/GraphEdge.java))
  - **Pure Data Manipulation**: Transformations through pure functions, explicit data flow
  - **Flat Data Models**: Denormalized structures for efficient processing
  - **Interleaved Arrays**: Better cache locality for related data pairs (e.g., [`RationalMatrix`](2025/src/main/java/info/jab/aoc2025/day10/RationalMatrix.java) with interleaved numerator/denominator storage)

- **Algorithms & Data Structures**:
  - **Graph Algorithms**: BFS (Breadth-First Search), DFS (Depth-First Search), Dijkstra's shortest path, A* pathfinding with heuristics, memoized path counting (e.g., [`GraphPathCounter`](2025/src/main/java/info/jab/aoc2025/day11/GraphPathCounter.java))
  - **Dynamic Programming**: Memoization for overlapping subproblems, optimal substructure exploitation (e.g., [`GraphPathCounter`](2025/src/main/java/info/jab/aoc2025/day11/GraphPathCounter.java), [`BeamPathCounter`](2025/src/main/java/info/jab/aoc2025/day7/BeamPathCounter.java))
  - **Backtracking**: Recursive search with pruning, constraint satisfaction (e.g., [`ShapePacking`](2025/src/main/java/info/jab/aoc2025/day12/ShapePacking.java) with bitmask optimization)
  - **State Space Search**: BFS/DFS for exploring solution spaces (e.g., elevator puzzles, game states)
  - **Range Algorithms**: Range merging, interval operations, coverage calculations (e.g., [`Range2`](2025/src/main/java/info/jab/aoc2025/day5/Range2.java), [`Interval`](2025/src/main/java/info/jab/aoc2025/day5/Interval.java))
  - **Tree Traversal**: File system navigation, hierarchical data processing
  - **String Algorithms**: Pattern matching, parsing, validation, hash generation
  - **Grid Algorithms**: 2D matrix manipulation, neighbor checking, cellular automata, pathfinding in grids (e.g., [`GridNeighbor2`](2025/src/main/java/info/jab/aoc2025/day4/GridNeighbor2.java), [`BeamPathCounter`](2025/src/main/java/info/jab/aoc2025/day7/BeamPathCounter.java))
  - **Union-Find (DSU)**: Disjoint Set Union for connected components with path compression and union by size (e.g., [`DSU`](2025/src/main/java/info/jab/aoc2025/day8/DSU.java) in [`PointCluster3`](2025/src/main/java/info/jab/aoc2025/day8/PointCluster3.java))
  - **Meet-in-the-Middle**: Split-and-combine approach for exponential problems (e.g., [`Part1Solver`](2025/src/main/java/info/jab/aoc2025/day10/Part1Solver.java) with Gray Code optimization)
  - **RREF (Reduced Row Echelon Form)**: Linear algebra for constraint optimization (e.g., [`RationalMatrix`](2025/src/main/java/info/jab/aoc2025/day10/RationalMatrix.java) in [`Part2Solver`](2025/src/main/java/info/jab/aoc2025/day10/Part2Solver.java))
  - **Bitmask Optimization**: Efficient 2D grid representation with O(1) bitwise operations (e.g., [`ShapePacking`](2025/src/main/java/info/jab/aoc2025/day12/ShapePacking.java), [`ShapeVariant`](2025/src/main/java/info/jab/aoc2025/day12/ShapeVariant.java))
  - **Primitive Collections**: FastUtil and Eclipse Collections for performance-critical code (e.g., [`ObjectArrayList`](2025/src/main/java/info/jab/aoc2025/day8/PointCluster3.java), [`LongIntHashMap`](2025/src/main/java/info/jab/aoc2025/day10/Part1Solver.java))
  - **Optimization Techniques**: Big O analysis, complexity optimization, space-time tradeoffs, parallel execution

## References

- https://openjdk.org/projects/code-tools/jol/
- https://jmh.morethan.io/
- https://inside.java/2024/05/23/dop-v1-1-introduction/
- https://adventofcode.com/
- https://www.reddit.com/r/adventofcode/
- https://www.reddit.com/r/adventofcode/?f=flair_name%3A%22Funny%22
- https://www.reddit.com/r/adventofcode/?f=flair_name%3A%22Visualization%22
- https://www.reddit.com/r/adventofcode/search/?q=flair_name%3A%22SOLUTION%20MEGATHREAD%22&restrict_sr=1

## Acknowledgements

A heartfelt thanks to ChatGPT and Gemini for the insightful early morning (6:00 AM)
discussions on design thinking and code refactoring, with a focus on functional approaches.
I'm also deeply grateful to my friend [Juan Antonio Medina](https://www.github.com/juan-medina)
for the continuous and inspiring conversations throughout the day,
and to [Rene van Putten](https://github.com/z669016/) for their invaluable inspiration.
