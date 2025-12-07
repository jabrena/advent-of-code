package info.jab.aoc2016.day3;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;

/**
 * Solver for Day 3: Squares With Three Sides
 * Validates triangles by checking if three sides can form a valid triangle.
 */
public final class TriangleValidator implements Solver<Integer> {

    @Override
    public Integer solvePartOne(final String fileName) {
        List<String> lines = ResourceLines.list("/" + fileName);
        
        int validTriangles = 0;
        
        for (String line : lines) {
            // Parse the three sides from the line
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 3) {
                int side1 = Integer.parseInt(parts[0]);
                int side2 = Integer.parseInt(parts[1]);
                int side3 = Integer.parseInt(parts[2]);
                
                // Check if it's a valid triangle
                if (isValidTriangle(side1, side2, side3)) {
                    validTriangles++;
                }
            }
        }
        
        return validTriangles;
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        List<String> lines = ResourceLines.list("/" + fileName);
        
        int validTriangles = 0;
        
        // Process lines in groups of 3 to read triangles vertically
        for (int i = 0; i < lines.size(); i += 3) {
            if (i + 2 < lines.size()) {
                // Parse three consecutive lines
                String[] line1Parts = lines.get(i).trim().split("\\s+");
                String[] line2Parts = lines.get(i + 1).trim().split("\\s+");
                String[] line3Parts = lines.get(i + 2).trim().split("\\s+");
                
                if (line1Parts.length >= 3 && line2Parts.length >= 3 && line3Parts.length >= 3) {
                    // Check each column as a triangle
                    for (int col = 0; col < 3; col++) {
                        int side1 = Integer.parseInt(line1Parts[col]);
                        int side2 = Integer.parseInt(line2Parts[col]);
                        int side3 = Integer.parseInt(line3Parts[col]);
                        
                        if (isValidTriangle(side1, side2, side3)) {
                            validTriangles++;
                        }
                    }
                }
            }
        }
        
        return validTriangles;
    }
    
    /**
     * Check if three sides can form a valid triangle.
     * In a valid triangle, the sum of any two sides must be larger than the remaining side.
     */
    private boolean isValidTriangle(final int a, final int b, final int c) {
        return (a + b > c) && (a + c > b) && (b + c > a);
    }
}

