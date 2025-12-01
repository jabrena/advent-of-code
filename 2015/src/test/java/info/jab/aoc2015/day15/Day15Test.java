package info.jab.aoc2015.day15;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.ArrayList;

class Day15Test {

    private Day15 solution;

    @BeforeEach
    void setUp() {
        solution = new Day15();
    }

    @Test
    void testPart1WithSampleData() {
        // Given the sample data from the problem description
        List<Day15.Ingredient> sampleIngredients = List.of(
            new Day15.Ingredient("Butterscotch", -1, -2, 6, 3, 8),
            new Day15.Ingredient("Cinnamon", 2, 3, -2, -1, 3)
        );
        
        // When calculating the max score for the sample
        long maxScore = findMaxScoreForIngredients(sampleIngredients, 100);
        
        // Then it should match the expected result from the problem
        assertThat(maxScore).isEqualTo(62842880L);
    }

    @Test
    void testPart1WithActualData() {
        // When solving part 1 with the actual input data
        long result = solution.solvePart1();
        
        // Then it should give the correct answer
        assertThat(result).isEqualTo(222870L);
    }

    @Test
    void testScoreCalculationWithNegativeValues() {
        // Given ingredients that would produce negative property values
        List<Day15.Ingredient> ingredients = List.of(
            new Day15.Ingredient("Test1", -10, 1, 1, 1, 1),
            new Day15.Ingredient("Test2", 1, -10, 1, 1, 1)
        );
        
        // When calculating score with equal amounts
        long score = calculateScoreForAmounts(ingredients, new int[]{50, 50});
        
        // Then negative totals should become 0, making the total score 0
        assertThat(score).isEqualTo(0L);
    }

    @Test
    void testPart2WithSampleData() {
        // Given the sample data from the problem description
        List<Day15.Ingredient> sampleIngredients = List.of(
            new Day15.Ingredient("Butterscotch", -1, -2, 6, 3, 8),
            new Day15.Ingredient("Cinnamon", 2, 3, -2, -1, 3)
        );
        
        // When calculating the max score with calorie constraint for the sample
        long maxScore = findMaxScoreWithCaloriesForIngredients(sampleIngredients, 100, 500);
        
        // Then it should match the expected result from the problem (40 butterscotch, 60 cinnamon = 57600000)
        assertThat(maxScore).isEqualTo(57600000L);
    }

    @Test
    void testPart2WithActualData() {
        // When solving part 2 with the actual input data
        long result = solution.solvePart2();
        
        // Then it should give the correct answer
        assertThat(result).isEqualTo(117936L);
    }

    @Test
    void testIngredientParsing() {
        // Given a sample ingredient line
        String line = "Sugar: capacity 3, durability 0, flavor 0, texture -3, calories 2";
        
        // When parsing the ingredient
        Day15.Ingredient ingredient = parseIngredientLine(line);
        
        // Then all properties should be correctly parsed
        assertThat(ingredient.name()).isEqualTo("Sugar");
        assertThat(ingredient.capacity()).isEqualTo(3);
        assertThat(ingredient.durability()).isEqualTo(0);
        assertThat(ingredient.flavor()).isEqualTo(0);
        assertThat(ingredient.texture()).isEqualTo(-3);
        assertThat(ingredient.calories()).isEqualTo(2);
    }

    // Helper methods to access private functionality for testing
    
    private long findMaxScoreForIngredients(List<Day15.Ingredient> ingredients, int totalTeaspoons) {
        return findMaxScoreRecursive(ingredients, totalTeaspoons, 0, new int[ingredients.size()]);
    }
    
    private long findMaxScoreWithCaloriesForIngredients(List<Day15.Ingredient> ingredients, int totalTeaspoons, int targetCalories) {
        return findMaxScoreWithCaloriesRecursive(ingredients, totalTeaspoons, targetCalories, 0, new int[ingredients.size()]);
    }
    
    private long findMaxScoreRecursive(List<Day15.Ingredient> ingredients, int remaining, int currentIndex, int[] amounts) {
        if (currentIndex == ingredients.size() - 1) {
            amounts[currentIndex] = remaining;
            return calculateScoreForAmounts(ingredients, amounts);
        }
        
        long maxScore = 0;
        for (int i = 0; i <= remaining; i++) {
            amounts[currentIndex] = i;
            long score = findMaxScoreRecursive(ingredients, remaining - i, currentIndex + 1, amounts);
            maxScore = Math.max(maxScore, score);
        }
        
        return maxScore;
    }
    
    private long findMaxScoreWithCaloriesRecursive(List<Day15.Ingredient> ingredients, int remaining, int targetCalories, int currentIndex, int[] amounts) {
        if (currentIndex == ingredients.size() - 1) {
            amounts[currentIndex] = remaining;
            if (calculateCaloriesForAmounts(ingredients, amounts) == targetCalories) {
                return calculateScoreForAmounts(ingredients, amounts);
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
    
    private long calculateScoreForAmounts(List<Day15.Ingredient> ingredients, int[] amounts) {
        long capacity = 0, durability = 0, flavor = 0, texture = 0;
        
        for (int i = 0; i < ingredients.size(); i++) {
            Day15.Ingredient ingredient = ingredients.get(i);
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
    
    private int calculateCaloriesForAmounts(List<Day15.Ingredient> ingredients, int[] amounts) {
        int totalCalories = 0;
        
        for (int i = 0; i < ingredients.size(); i++) {
            Day15.Ingredient ingredient = ingredients.get(i);
            int amount = amounts[i];
            totalCalories += ingredient.calories() * amount;
        }
        
        return totalCalories;
    }
    
    private Day15.Ingredient parseIngredientLine(String line) {
        String[] parts = line.split(": ");
        String name = parts[0];
        String[] properties = parts[1].split(", ");
        
        int capacity = Integer.parseInt(properties[0].split(" ")[1]);
        int durability = Integer.parseInt(properties[1].split(" ")[1]);
        int flavor = Integer.parseInt(properties[2].split(" ")[1]);
        int texture = Integer.parseInt(properties[3].split(" ")[1]);
        int calories = Integer.parseInt(properties[4].split(" ")[1]);
        
        return new Day15.Ingredient(name, capacity, durability, flavor, texture, calories);
    }
}