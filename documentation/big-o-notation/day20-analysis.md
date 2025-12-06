# Day20 Performance Analysis and Optimization

## Problem Description

Find the first house number where the sum of presents delivered by elves equals or exceeds a target value (34,000,000).

**Part 1**: Each elf `e` visits houses `e, 2e, 3e, ...` and delivers `10*e` presents to each.
**Part 2**: Each elf `e` visits houses `e, 2e, 3e, ..., 50e` and delivers `11*e` presents to each.

## Current Implementation Analysis

### Algorithm
- Sequential house-by-house checking
- For each house H, calculate all divisors (O(√H))
- Sum presents from all divisors

### Complexity
- **Time**: O(H × √H) where H is the final house number found
- **Space**: O(1)

### Current Performance
- For target = 34,000,000, expected house number ≈ 786,240 (Part 1) and 831,600 (Part 2)
- Each house calculation: O(√H) ≈ O(√800,000) ≈ O(900) operations
- Total operations: ~800,000 × 900 ≈ 720 million operations

## Optimization Opportunity: Sieve-Based Approach

### Algorithm
Instead of calculating divisors for each house individually, iterate through elves and add presents to all houses they visit.

**Part 1**:
```java
for (int elf = 1; elf <= H; elf++) {
    for (int house = elf; house <= H; house += elf) {
        presents[house] += elf * 10;
    }
}
```

**Part 2**:
```java
for (int elf = 1; elf <= H; elf++) {
    for (int house = elf; house <= min(elf * 50, H); house += elf) {
        presents[house] += elf * 11;
    }
}
```

### Complexity Analysis

**Part 1**:
- For each elf `e`, we visit `H/e` houses
- Total operations: H/1 + H/2 + H/3 + ... + H/H = H × (1 + 1/2 + 1/3 + ... + 1/H)
- The harmonic series sum is O(log H)
- **Time**: O(H log H)
- **Space**: O(H) for the presents array

**Part 2**:
- Similar to Part 1, but limited to 50 houses per elf
- For elf `e`, we visit min(50, H/e) houses
- Still O(H log H) but with better constant factor
- **Time**: O(H log H)
- **Space**: O(H)

### Complexity Comparison

| Approach | Time Complexity | Space Complexity | Notes |
|----------|----------------|------------------|-------|
| **Current** | O(H × √H) | O(1) | Sequential divisor calculation |
| **Sieve** | O(H log H) | O(H) | Pre-compute all houses at once |

### Performance Improvement Estimate

For H ≈ 800,000:
- **Current**: O(H × √H) ≈ O(800,000 × 900) ≈ O(720M operations)
- **Sieve**: O(H log H) ≈ O(800,000 × 20) ≈ O(16M operations)
- **Expected speedup**: ~45x theoretical improvement

However, practical considerations:
- Sieve requires O(H) memory (≈3MB for H=800,000)
- Cache locality: sieve has better memory access patterns
- Early termination: current approach can stop immediately, sieve needs to process all houses up to target

**Realistic improvement**: ~2-5x faster for typical inputs

## Implementation Considerations

### Advantages of Sieve Approach
1. **Better complexity**: O(H log H) vs O(H × √H)
2. **Better cache locality**: Sequential memory access pattern
3. **Parallelizable**: Can process different elf ranges in parallel
4. **Scalable**: Better for larger inputs

### Disadvantages of Sieve Approach
1. **Memory overhead**: Requires O(H) array
2. **No early termination**: Must process all houses up to target
3. **More complex**: Requires estimating upper bound

### Hybrid Approach
A hybrid approach could combine both:
1. Use sieve for initial range (e.g., first 100,000 houses)
2. Switch to sequential checking if target not found
3. This provides early termination while benefiting from sieve efficiency

## Benchmark Results (Estimated)

For target = 34,000,000:

| Approach | Part 1 Time | Part 2 Time | Memory |
|----------|-------------|------------|--------|
| **Current** | ~2-3 seconds | ~2-3 seconds | O(1) |
| **Sieve** | ~0.5-1 second | ~0.5-1 second | O(H) ≈ 3MB |
| **Improvement** | ~2-3x faster | ~2-3x faster | +3MB |

## Recommendation

**Yes, optimization is worthwhile** for the following reasons:

1. **Significant complexity improvement**: O(H log H) vs O(H × √H)
2. **Practical speedup**: 2-5x faster for typical inputs
3. **Better scalability**: Performance gap increases for larger inputs
4. **Acceptable memory cost**: Only ~3MB for typical inputs

The sieve approach is a classic optimization for divisor-sum problems and is well-suited for this use case.

## Code Changes Required

1. Create optimized version using sieve approach
2. Add upper bound estimation logic
3. Consider hybrid approach for early termination
4. Add performance benchmarks to validate improvement
