package info.jab.aoc.day15;

import java.util.ArrayList;
import java.util.List;

public class Day15 {
    
    public static void main(String[] args) {
        Day15 solution = new Day15();
        long result1 = solution.solvePart1();
        System.out.println("Part 1 result: " + result1);
        
        long result2 = solution.solvePart2();
        System.out.println("Part 2 result: " + result2);
    }
    
    public long solvePart1() {
        List<Ingredient> ingredients = parseInput();
        return findMaxScore(ingredients, 100);
    }
    
    public long solvePart2() {
        List<Ingredient> ingredients = parseInput();
        return findMaxScoreWithCalories(ingredients, 100, 500);
    }
    
    private List<Ingredient> parseInput() {
        // Input data from the AOC client
        String input = """
            Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2
            Sprinkles: capacity -3, durability 3, flavor 0, texture 0, calories 9
            Candy: capacity -1, durability 0, flavor 4, texture 0, calories 1
            Chocolate: capacity 0, durability 0, flavor -2, texture 2, calories 8
            """;
        
        List<Ingredient> ingredients = new ArrayList<>();
        String[] lines = input.trim().split("\n");
        
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
        for (int i = 0; i <= remaining; i++) {
            amounts[currentIndex] = i;
            long score = findMaxScoreRecursive(ingredients, remaining - i, currentIndex + 1, amounts);
            maxScore = Math.max(maxScore, score);
        }
        
        return maxScore;
    }
    
    private long findMaxScoreWithCaloriesRecursive(List<Ingredient> ingredients, int remaining, int targetCalories, int currentIndex, int[] amounts) {
        if (currentIndex == ingredients.size() - 1) {
            // Last ingredient gets all remaining teaspoons
            amounts[currentIndex] = remaining;
            if (calculateCalories(ingredients, amounts) == targetCalories) {
                return calculateScore(ingredients, amounts);
            } else {
                return 0;
            }
        }
        
        long maxScore = 0;
        for (int i = 0; i <= remaining; i++) {
            amounts[currentIndex] = i;
            long score = findMaxScoreWithCaloriesRecursive(ingredients, remaining - i, targetCalories, currentIndex + 1, amounts);
            maxScore = Math.max(maxScore, score);
        }
        
        return maxScore;
    }
    
    private long calculateScore(List<Ingredient> ingredients, int[] amounts) {
        long capacity = 0, durability = 0, flavor = 0, texture = 0;
        
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
    
    public record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {}
}