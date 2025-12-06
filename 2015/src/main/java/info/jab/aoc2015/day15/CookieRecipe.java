package info.jab.aoc2015.day15;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;
import info.jab.aoc.Trampoline;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Solver for cookie recipe optimization.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Stream API for declarative transformations
 * - Immutable data structures
 */
public final class CookieRecipe implements Solver<Long> {

    private static final int TOTAL_TEASPOONS = 100;
    private static final int TARGET_CALORIES = 500;


    @Override
    public Long solvePartOne(final String fileName) {
        final List<Ingredient> ingredients = parseInput(fileName);
        return findMaxScore(ingredients, TOTAL_TEASPOONS);
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        final List<Ingredient> ingredients = parseInput(fileName);
        return findMaxScoreWithCalories(ingredients, TOTAL_TEASPOONS, TARGET_CALORIES);
    }

    /**
     * Pure function: parses ingredients from input file using stream API.
     */
    private List<Ingredient> parseInput(final String fileName) {
        return ResourceLines.list(fileName).stream()
                .map(this::parseIngredient)
                .toList();
    }
    
    /**
     * Pure function: parses a single ingredient line.
     */
    private Ingredient parseIngredient(final String line) {
        // Parse line like: "Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2"
        final String[] parts = line.split(": ");
        final String name = parts[0];
        final String[] properties = parts[1].split(", ");
        
        return new Ingredient(
                name,
                Integer.parseInt(properties[0].split(" ")[1]),
                Integer.parseInt(properties[1].split(" ")[1]),
                Integer.parseInt(properties[2].split(" ")[1]),
                Integer.parseInt(properties[3].split(" ")[1]),
                Integer.parseInt(properties[4].split(" ")[1])
        );
    }
    
    /**
     * Pure function: finds maximum score using trampoline pattern for safe recursion.
     */
    private long findMaxScore(final List<Ingredient> ingredients, final int totalTeaspoons) {
        return Trampoline.run(findMaxScoreTrampoline(ingredients, totalTeaspoons, 0, List.of()));
    }
    
    /**
     * Pure function: finds maximum score with calorie constraint using trampoline pattern.
     */
    private long findMaxScoreWithCalories(final List<Ingredient> ingredients, final int totalTeaspoons, final int targetCalories) {
        return Trampoline.run(findMaxScoreWithCaloriesTrampoline(ingredients, totalTeaspoons, targetCalories, 0, List.of()));
    }
    
    /**
     * Creates a trampoline computation for finding maximum score.
     * Uses tail-recursive pattern converted to trampoline for stack safety.
     */
    private Trampoline<Long> findMaxScoreTrampoline(
            final List<Ingredient> ingredients,
            final int remaining,
            final int currentIndex,
            final List<Integer> amounts) {
        
        if (currentIndex == ingredients.size() - 1) {
            // Last ingredient gets all remaining teaspoons
            final List<Integer> finalAmounts = append(amounts, remaining);
            return new Trampoline.Done<>(calculateScore(ingredients, finalAmounts).totalScore());
        }
        
        // Try all possible amounts for current ingredient using stream
        // Each branch returns a trampoline, then we find the max
        final List<Trampoline<Long>> branchTrampolines = IntStream.rangeClosed(0, remaining)
                .mapToObj(amount -> {
                    final List<Integer> newAmounts = append(amounts, amount);
                    return findMaxScoreTrampoline(ingredients, remaining - amount, currentIndex + 1, newAmounts);
                })
                .toList();
        
        // Combine all branch trampolines and find max
        return combineTrampolinesAndFindMax(branchTrampolines);
    }
    
    /**
     * Creates a trampoline computation for finding maximum score with calorie constraint.
     * Uses tail-recursive pattern converted to trampoline for stack safety.
     */
    private Trampoline<Long> findMaxScoreWithCaloriesTrampoline(
            final List<Ingredient> ingredients,
            final int remaining,
            final int targetCalories,
            final int currentIndex,
            final List<Integer> amounts) {
        
        if (currentIndex == ingredients.size() - 1) {
            // Last ingredient gets all remaining teaspoons
            final List<Integer> finalAmounts = append(amounts, remaining);
            final int totalCalories = calculateCalories(ingredients, finalAmounts);
            if (totalCalories == targetCalories) {
                return new Trampoline.Done<>(calculateScore(ingredients, finalAmounts).totalScore());
            }
            return new Trampoline.Done<>(0L);
        }
        
        // Try all possible amounts for current ingredient using stream
        final List<Trampoline<Long>> branchTrampolines = IntStream.rangeClosed(0, remaining)
                .mapToObj(amount -> {
                    final List<Integer> newAmounts = append(amounts, amount);
                    return findMaxScoreWithCaloriesTrampoline(
                            ingredients,
                            remaining - amount,
                            targetCalories,
                            currentIndex + 1,
                            newAmounts
                    );
                })
                .toList();
        
        // Combine all branch trampolines and find max
        return combineTrampolinesAndFindMax(branchTrampolines);
    }
    
    /**
     * Combines multiple trampolines and finds the maximum value.
     * Evaluates all branch trampolines fully, then finds the maximum result.
     * This maintains trampoline safety while exploring all branches.
     */
    private Trampoline<Long> combineTrampolinesAndFindMax(final List<Trampoline<Long>> trampolines) {
        if (trampolines.isEmpty()) {
            return new Trampoline.Done<>(0L);
        }
        
        // Check if all trampolines are Done (fully evaluated)
        final boolean allDone = trampolines.stream().allMatch(t -> t instanceof Trampoline.Done<Long>);
        
        if (allDone) {
            // All branches are evaluated, find the maximum
            final long max = trampolines.stream()
                    .map(t -> ((Trampoline.Done<Long>) t).result())
                    .max(Long::compareTo)
                    .orElse(0L);
            return new Trampoline.Done<>(max);
        }
        
        // Some branches still need evaluation, continue with one step
        return new Trampoline.More<>(() -> {
            final List<Trampoline<Long>> evaluated = trampolines.stream()
                    .map(t -> {
                        if (t instanceof Trampoline.More<Long> more) {
                            return more.compute().get();
                        }
                        return t; // Already Done, keep as is
                    })
                    .toList();
            return combineTrampolinesAndFindMax(evaluated);
        });
    }
    
    /**
     * Pure function: creates new list with appended element (immutable).
     */
    private List<Integer> append(final List<Integer> list, final int value) {
        return Stream.concat(list.stream(), Stream.of(value))
                .toList();
    }
    
    /**
     * Pure function: calculates recipe score from ingredients and amounts.
     */
    private RecipeScore calculateScore(final List<Ingredient> ingredients, final List<Integer> amounts) {
        return IntStream.range(0, ingredients.size())
                .boxed()
                .reduce(
                        new RecipeScore(0L, 0L, 0L, 0L),
                        (score, i) -> {
                            final Ingredient ingredient = ingredients.get(i);
                            final int amount = amounts.get(i);
                            return new RecipeScore(
                                    score.capacity() + (long) ingredient.capacity() * amount,
                                    score.durability() + (long) ingredient.durability() * amount,
                                    score.flavor() + (long) ingredient.flavor() * amount,
                                    score.texture() + (long) ingredient.texture() * amount
                            );
                        },
                        (s1, s2) -> new RecipeScore(
                                s1.capacity() + s2.capacity(),
                                s1.durability() + s2.durability(),
                                s1.flavor() + s2.flavor(),
                                s1.texture() + s2.texture()
                        )
                );
    }
    
    /**
     * Pure function: calculates total calories using stream.
     */
    private int calculateCalories(final List<Ingredient> ingredients, final List<Integer> amounts) {
        return IntStream.range(0, ingredients.size())
                .map(i -> ingredients.get(i).calories() * amounts.get(i))
                .sum();
    }
}
