# Advent of Code Problem Type Classification

This directory contains classification documents that categorize Advent of Code problems by their primary problem-solving approach and data structures.

## Purpose

The classification system helps:
- **Understand problem patterns**: Identify common problem types across different years
- **Choose appropriate algorithms**: Select the right approach based on problem characteristics
- **Track problem distribution**: See which types of problems appear most frequently
- **Study problem evolution**: Observe how problem types change over years

## Classification Categories

### 1. Data Processing

**Definition**: Problems that primarily involve parsing, transforming, filtering, or aggregating data without complex spatial or algorithmic structures.

**Characteristics**:
- String manipulation and parsing
- Data transformation and filtering
- Aggregation operations (sum, count, max, min)
- Set operations (intersection, union)
- Tree/list traversal for data extraction
- Virtual machine execution
- Hash generation and validation
- Pattern matching and validation

**Common Operations**:
- Parsing input formats
- Sorting and grouping
- Counting and aggregating
- String pattern matching
- Set operations
- Stack/queue operations
- Tree traversal (for data extraction, not spatial)

**Examples**:
- **2015 Day1**: Character counting, floor tracking
- **2015 Day2**: Dimension parsing, area calculation
- **2016 Day1**: Position tracking, distance calculation
- **2022 Day1**: Calorie grouping and sorting
- **2022 Day7**: File system tree traversal
- **2024 Day1**: List sorting and distance calculations

**When to classify as Data Processing**:
- Primary focus is on data transformation
- No 2D grid manipulation required
- No complex recursive algorithms or game simulation
- Operations are primarily linear or near-linear

---

### 2. Grid (2D Matrix)

**Definition**: Problems that involve 2D grid/matrix manipulation, pathfinding, spatial operations, or cellular automata.

**Characteristics**:
- 2D grid/matrix data structure
- Spatial relationships between cells
- Pathfinding algorithms (BFS, DFS, Dijkstra, A*)
- Grid traversal and manipulation
- Visibility calculations
- Coverage area calculations
- Cellular automaton simulations
- Position tracking in 2D space

**Common Operations**:
- Grid traversal (row/column iteration)
- Neighbor checking (4-directional or 8-directional)
- Pathfinding (shortest path, reachability)
- Visibility calculations
- Grid state evolution
- Boundary checking
- Coordinate transformations

**Examples**:
- **2015 Day6**: 2D grid light manipulation
- **2015 Day18**: Conway's Game of Life (cellular automaton)
- **2016 Day8**: 2D screen manipulation
- **2016 Day13**: BFS pathfinding in dynamically generated maze
- **2022 Day8**: Tree visibility in 2D grid
- **2024 Day4**: Word search in 2D grid
- **2024 Day16**: A*/Dijkstra pathfinding in grid

**When to classify as Grid (2D Matrix)**:
- Uses 2D array/grid as primary data structure
- Involves spatial relationships or pathfinding
- Requires neighbor checking or grid traversal
- Problems about visibility, coverage, or movement in 2D space

**Note**: Simple position tracking (like Day1 2016 tracking Santa's position) without a grid structure is classified as Data Processing, not Grid.

---

### 3. Games & Algorithms

**Definition**: Problems involving game simulation, game rules, or complex algorithmic challenges like recursion, backtracking, dynamic programming, graph algorithms, or mathematical optimization.

**Characteristics**:
- Game simulation with rules and state
- Recursive algorithms
- Backtracking search
- Dynamic programming
- Graph algorithms (shortest path, cycles, cliques)
- Permutation/combination generation
- Constraint optimization
- State space search
- Mathematical problem-solving

**Common Operations**:
- Recursive function calls
- Backtracking with pruning
- Memoization
- State space exploration
- Game rule simulation
- Permutation/combination generation
- Graph traversal (beyond simple pathfinding)
- Mathematical formula application

**Examples**:
- **2015 Day7**: Recursive backtracking for operator combinations
- **2015 Day9**: Traveling Salesman Problem (permutations)
- **2015 Day11**: Memoized recursion for stone transformations
- **2015 Day19**: Dynamic programming for pattern matching
- **2015 Day22**: Wizard battle simulation with state space search
- **2016 Day11**: BFS state space search (elevator puzzle)
- **2016 Day19**: Josephus problem (mathematical)
- **2024 Day7**: Recursive backtracking
- **2024 Day23**: Graph algorithms (clique finding)

**When to classify as Games & Algorithms**:
- Involves game simulation with complex rules
- Requires recursive algorithms or backtracking
- Uses dynamic programming or memoization
- Involves graph algorithms beyond simple pathfinding
- Requires generating permutations/combinations
- Involves mathematical optimization problems

**Distinction from Grid (2D Matrix)**:
- Grid problems focus on spatial manipulation and pathfinding
- Games & Algorithms focus on rule-based simulation, recursive algorithms, or mathematical optimization
- Some problems may involve grids but are classified here if the primary challenge is algorithmic (e.g., state space search, recursion)

---

### 4. Others

**Definition**: Problems that don't fit into the above categories.

**Current Status**: No problems are currently classified in this category. All problems fit into one of the three main categories.

**Potential Future Use**:
- Problems involving 3D spatial operations
- Problems involving complex graph structures beyond 2D grids
- Problems with unique characteristics not covered by other categories

---

## Classification Guidelines

### Primary Type vs. Part Types

Each problem has:
- **Primary Type**: The overall classification based on the problem's main characteristics
- **Part 1 Type**: Classification for Part 1 (may differ from Part 2)
- **Part 2 Type**: Classification for Part 2 (may differ from Part 1)

**Examples of Mixed Types**:
- **2015 Day17**: Part 1 is Data Processing (VM execution), Part 2 is Games & Algorithms (backtracking)
- **2015 Day19**: Part 1 is Data Processing (molecule generation), Part 2 is Games & Algorithms (reverse search)
- **2024 Day17**: Part 1 is Data Processing (VM execution), Part 2 is Games & Algorithms (backtracking)

### Classification Decision Process

1. **Identify the primary data structure**:
   - 2D grid/matrix → Grid (2D Matrix)
   - Lists, strings, trees (for data) → Data Processing
   - Complex state spaces, graphs → Games & Algorithms

2. **Identify the primary algorithm**:
   - Pathfinding, grid traversal → Grid (2D Matrix)
   - Parsing, filtering, aggregation → Data Processing
   - Recursion, backtracking, DP → Games & Algorithms

3. **Consider the problem's focus**:
   - Spatial relationships → Grid (2D Matrix)
   - Data transformation → Data Processing
   - Algorithmic complexity → Games & Algorithms

### Edge Cases

**Position Tracking Without Grid**:
- Problems that track 2D positions but don't use a grid structure are classified as **Data Processing**
- Example: 2016 Day1 (Santa's position tracking)

**Grid Problems with Algorithmic Focus**:
- If a problem uses a grid but the primary challenge is algorithmic (e.g., state space search), classify as **Games & Algorithms**
- Example: 2016 Day11 (elevator puzzle uses floors but is primarily state space search)

**Virtual Machine Execution**:
- Virtual machine execution is typically **Data Processing** (instruction execution, register manipulation)
- Exception: If VM execution involves backtracking or state space search, classify as **Games & Algorithms**

---

## Usage

### Reading Classification Documents

Each year has its own classification document:
- `2015.md` - Advent of Code 2015 problems
- `2016.md` - Advent of Code 2016 problems
- `2022.md` - Advent of Code 2022 problems
- `2024.md` - Advent of Code 2024 problems
- `2025.md` - Advent of Code 2025 problems

### Document Structure

Each document contains:
1. **Classification Table**: All problems with their types
2. **Summary by Type**: Count and list of problems per type
3. **Detailed Analysis**: Explanation for each problem's classification

### Finding Similar Problems

To find problems of a specific type:
1. Open the relevant year's classification document
2. Check the "Summary by Type" section
3. Review the "Detailed Analysis" for specific problem details

### Comparing Across Years

To see how problem types are distributed:
1. Check the "Summary by Type" in each year's document
2. Compare counts across years
3. Review detailed analyses to understand trends

---

## Statistics Summary

### Overall Distribution (All Years Combined)

Based on the classification documents:

- **Data Processing**: ~60-70% of problems
- **Grid (2D Matrix)**: ~20-30% of problems
- **Games & Algorithms**: ~10-20% of problems

### Year-by-Year Highlights

- **2015**: Strong focus on Games & Algorithms (permutations, combinations, recursion)
- **2016**: Balanced mix, many virtual machine problems
- **2022**: Heavy focus on Data Processing
- **2024**: Good mix with many grid pathfinding problems
- **2025**: Early days, mostly Data Processing so far

---

## Contributing

When adding new problems or updating classifications:

1. **Read the problem carefully**: Understand the primary challenge
2. **Identify the main data structure**: Grid, list, tree, graph, etc.
3. **Identify the main algorithm**: Parsing, pathfinding, recursion, etc.
4. **Classify based on primary focus**: Don't overthink edge cases
5. **Document your reasoning**: Add notes explaining the classification

### Classification Checklist

- [ ] Primary data structure identified
- [ ] Primary algorithm identified
- [ ] Classification matches guidelines
- [ ] Notes explain any edge cases
- [ ] Both Part 1 and Part 2 considered

---

## References

- [Advent of Code](https://adventofcode.com/) - The original problem source
- Big O Notation Analysis - See `../big-o-notation/` for complexity analysis
- Problem Solutions - See year-specific directories for implementations

---

*Last updated: 2025-12-03*
*For specific year classifications, see the corresponding year files (2015.md, 2016.md, 2022.md, 2024.md, 2025.md)*
