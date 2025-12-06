package info.jab.aoc2015.day15;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayList;
import java.util.List;

public final class CookieRecipeSolver implements Solver<Long> {

    private static final int TOTAL_TEASPOONS = 100;
    private static final int TARGET_CALORIES = 500;

    public record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {}

    @Override
    public Long solvePartOne(final String fileName) {
        List<Ingredient> ingredients = parseInput(fileName);
        return findMaxScore(ingredients, TOTAL_TEASPOONS);
    }

    @Override
    public Long solvePartTwo(final String fileName) {
        List<Ingredient> ingredients = parseInput(fileName);
        return findMaxScoreWithCalories(ingredients, TOTAL_TEASPOONS, TARGET_CALORIES);
    }

    private List<Ingredient> parseInput(String fileName) {
        List<String> lines = ResourceLines.list(fileName);
        List<Ingredient> ingredients = new ArrayList<>();
        
        for (String line : lines) {
            ingredients.add(parseIngredient(line));
        }
        
        return ingredients;
    }
    
    private Ingredient parseIngredient(String line) {
        // Parse line like: "Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2"
        String[] parts = line.split(": ");
        String name = parts[0];
        String[] properties = parts[1].split(", ");
        
        int capacity = Integer.parseInt(properties[0].split(" ")[1]);
        int durability = Integer.parseInt(properties[1].split(" ")[1]);
        int flavor = Integer.parseInt(properties[2].split(" ")[1]);
        int texture = Integer.parseInt(properties[3].split(" ")[1]);
        int calories = Integer.parseInt(properties[4].split(" ")[1]);
        
        return new Ingredient(name, capacity, durability, flavor, texture, calories);
    }
    
    private long findMaxScore(List<Ingredient> ingredients, int totalTeaspoons) {
        return findMaxScoreRecursive(ingredients, totalTeaspoons, 0, new int[ingredients.size()]);
    }
    
    private long findMaxScoreWithCalories(List<Ingredient> ingredients, int totalTeaspoons, int targetCalories) {
        return findMaxScoreWithCaloriesRecursive(ingredients, totalTeaspoons, targetCalories, 0, new int[ingredients.size()]);
    }
    
    private long findMaxScoreRecursive(List<Ingredient> ingredients, int remaining, int currentIndex, int[] amounts) {
        if (currentIndex == ingredients.size() - 1) {
            // Last ingredient gets all remaining teaspoons
            amounts[currentIndex] = remaining;
            return calculateScore(ingredients, amounts);
        }
        
        long maxScore = 0;
        // Try all possible amounts for current ingredient
        for (int amount = 0; amount <= remaining; amount++) {
            amounts[currentIndex] = amount;
            long score = findMaxScoreRecursive(ingredients, remaining - amount, currentIndex + 1, amounts);
            maxScore = Math.max(maxScore, score);
        }
        
        return maxScore;
    }
    
    private long findMaxScoreWithCaloriesRecursive(
        List<Ingredient> ingredients,
        int remaining,
        int targetCalories,
        int currentIndex,
        int[] amounts) {
        
        if (currentIndex == ingredients.size() - 1) {
            // Last ingredient gets all remaining teaspoons
            amounts[currentIndex] = remaining;
            int totalCalories = calculateCalories(ingredients, amounts);
            if (totalCalories == targetCalories) {
                return calculateScore(ingredients, amounts);
            }
            return 0;
        }
        
        long maxScore = 0;
        // Try all possible amounts for current ingredient
        for (int amount = 0; amount <= remaining; amount++) {
            amounts[currentIndex] = amount;
            long score = findMaxScoreWithCaloriesRecursive(
                ingredients,
                remaining - amount,
                targetCalories,
                currentIndex + 1,
                amounts
            );
            maxScore = Math.max(maxScore, score);
        }
        
        return maxScore;
    }
    
    private long calculateScore(List<Ingredient> ingredients, int[] amounts) {
        long capacity = 0;
        long durability = 0;
        long flavor = 0;
        long texture = 0;
        
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            int amount = amounts[i];
            
            capacity += (long) ingredient.capacity() * amount;
            durability += (long) ingredient.durability() * amount;
            flavor += (long) ingredient.flavor() * amount;
            texture += (long) ingredient.texture() * amount;
        }
        
        // Negative totals become 0
        capacity = Math.max(0, capacity);
        durability = Math.max(0, durability);
        flavor = Math.max(0, flavor);
        texture = Math.max(0, texture);
        
        return capacity * durability * flavor * texture;
    }
    
    private int calculateCalories(List<Ingredient> ingredients, int[] amounts) {
        int totalCalories = 0;
        
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            int amount = amounts[i];
            totalCalories += ingredient.calories() * amount;
        }
        
        return totalCalories;
    }
}
