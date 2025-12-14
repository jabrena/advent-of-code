package info.jab.aoc2025.day1;

import java.util.List;

import info.jab.aoc.Solver;
import com.putoet.resources.ResourceLines;
import io.github.vmzakharov.ecdataframe.dataframe.DataFrame;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableBooleanList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.BooleanArrayList;

/**
 * Handles dial rotation logic for a 0-99 dial using a complete DataFrame-ec approach.
 * The dial can be rotated left (L) or right (R) by a specified distance.
 * <p>
 * This implementation uses DataFrame operations throughout:
 * - Computed columns for data transformation
 * - DataFrame filtering operations
 * - Stateful processing using DataFrame collect
 */
public final class DialRotatorV2 implements Solver<Integer> {

    private static final int DIAL_MAX = 100;
    private static final int INITIAL_POSITION = 50;

    /**
     * Counts how many times the dial points at 0 after each complete rotation.
     * Each rotation is applied as a single operation.
     * <p>
     * DataFrame-ec approach:
     * 1. Load data into DataFrame
     * 2. Compute structured columns (direction, distance, isValid)
     * 3. Filter valid rotations using DataFrame operations
     * 4. Process statefully using DataFrame collect
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 after rotations
     */
    @Override
    public Integer solvePartOne(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);

        // Step 1: Create DataFrame with raw rotation strings
        DataFrame df = new DataFrame("Rotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

        // Step 2: Compute structured columns using DataFrame collect
        final MutableList<String> directions = Lists.mutable.empty();
        final MutableIntList distances = new IntArrayList();
        final MutableBooleanList isValid = new BooleanArrayList();

        df.collect(cursor -> {
            final String rotationStr = cursor.getString("rotation");
            final boolean valid = isValidRotation(rotationStr);
            isValid.add(valid);
            if (valid) {
                final Rotation rotation = Rotation.from(rotationStr);
                // Extract direction char directly from rotation string (L or R)
                directions.add(String.valueOf(rotationStr.charAt(0)));
                distances.add(rotation.distance());
            } else {
                directions.add(null);
                distances.add(0);
            }
            return null;
        });

        // Step 3: Create structured DataFrame with computed columns
        DataFrame structuredDf = new DataFrame("StructuredRotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations))
                .addStringColumn("direction", directions)
                .addIntColumn("distance", distances)
                .addBooleanColumn("isValid", isValid);

        // Step 4: Filter valid rotations and process statefully using DataFrame collect
        // DataFrame-centric: filter and process in one pass
        final int[] position = {INITIAL_POSITION};
        final MutableList<Integer> zeroCounts = Lists.mutable.empty();

        structuredDf.collect(
                () -> null,
                (acc, cursor) -> {
                    // Filter: only process valid rotations
                    if (cursor.getBoolean("isValid")) {
                        // Process: apply rotation and compute zeroCount
                        final String directionStr = cursor.getString("direction");
                        final int distance = (int) cursor.getInt("distance");
                        final Direction direction = Direction.from(directionStr.charAt(0));

                        position[0] = rotateDial(position[0], direction, distance);
                        zeroCounts.add(position[0] == 0 ? 1 : 0);
                    }
                }
        );

        // Step 5: Aggregate zeroCount using Eclipse Collections (DataFrame-ec ecosystem)
        return (int) zeroCounts.sumOfInt(Integer::intValue);
    }

    /**
     * Counts how many times the dial points at 0 during rotations.
     * Optimized to calculate zero crossings directly without expanding rotations.
     * <p>
     * DataFrame-ec approach:
     * 1. Load data into DataFrame
     * 2. Compute structured columns (direction, distance, isValid)
     * 3. Filter valid rotations using DataFrame operations
     * 4. Process statefully with zero crossing calculation using DataFrame collect
     * Complexity: O(n) instead of O(n×d) by using modular arithmetic to count zero crossings.
     *
     * @param fileName File name containing rotation strings
     * @return The count of times the dial points at 0 during rotations
     */
    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> rotations = ResourceLines.list(fileName);

        // Step 1: Create DataFrame with raw rotation strings
        DataFrame df = new DataFrame("Rotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations));

        // Step 2: Compute structured columns using DataFrame collect
        final MutableList<String> directions = Lists.mutable.empty();
        final MutableIntList distances = new IntArrayList();
        final MutableBooleanList isValid = new BooleanArrayList();

        df.collect(cursor -> {
            final String rotationStr = cursor.getString("rotation");
            final boolean valid = isValidRotation(rotationStr);
            isValid.add(valid);
            if (valid) {
                final Rotation rotation = Rotation.from(rotationStr);
                // Extract direction char directly from rotation string (L or R)
                directions.add(String.valueOf(rotationStr.charAt(0)));
                distances.add(rotation.distance());
            } else {
                directions.add(null);
                distances.add(0);
            }
            return null;
        });

        // Step 3: Create structured DataFrame with computed columns
        DataFrame structuredDf = new DataFrame("StructuredRotations")
                .addStringColumn("rotation", Lists.mutable.ofAll(rotations))
                .addStringColumn("direction", directions)
                .addIntColumn("distance", distances)
                .addBooleanColumn("isValid", isValid);

        // Step 4: Filter valid rotations and process statefully using DataFrame collect
        // DataFrame-centric: filter and process in one pass
        final int[] position = {INITIAL_POSITION};
        final MutableList<Integer> zeroCrossings = Lists.mutable.empty();

        structuredDf.collect(
                () -> null,
                (acc, cursor) -> {
                    // Filter: only process valid rotations
                    if (cursor.getBoolean("isValid")) {
                        // Process: apply rotation with zero crossing calculation
                        final String directionStr = cursor.getString("direction");
                        final int distance = (int) cursor.getInt("distance");
                        final Direction direction = Direction.from(directionStr.charAt(0));

                        final int crossings = countZeroCrossings(position[0], direction, distance);
                        position[0] = rotateDial(position[0], direction, distance);
                        zeroCrossings.add(crossings);
                    }
                }
        );

        // Step 5: Aggregate zeroCrossings using Eclipse Collections (DataFrame-ec ecosystem)
        return (int) zeroCrossings.sumOfInt(Integer::intValue);
    }

    /**
     * Checks if a rotation string is valid (not null and not empty after trimming).
     * Optimized: avoids creating new string with trim(), checks length first for fast path.
     *
     * @param rotation Rotation string to validate
     * @return true if rotation is valid, false otherwise
     */
    private boolean isValidRotation(final String rotation) {
        if (rotation == null || rotation.isEmpty()) {
            return false;
        }
        // Fast path: if string has length > 0 and first char is not whitespace, it's valid
        if (!Character.isWhitespace(rotation.charAt(0))) {
            return true;
        }
        // Slow path: check if any non-whitespace character exists
        for (int i = 1; i < rotation.length(); i++) {
            if (!Character.isWhitespace(rotation.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates how many times position 0 is visited during a rotation.
     * Uses modular arithmetic to count zero crossings without expanding rotations.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @param distance Number of positions to rotate
     * @return Number of times position 0 is visited during the rotation
     */
    int countZeroCrossings(final int currentPosition, final Direction direction, final int distance) {
        return switch (direction) {
            // RIGHT: count multiples of DIAL_MAX in range [currentPosition+1, currentPosition+distance]
            case RIGHT -> currentPosition == 0
                    ? distance / DIAL_MAX
                    : (currentPosition + distance) / DIAL_MAX;
            // LEFT: count when we cross 0 going backwards
            case LEFT -> {
                if (currentPosition == 0) {
                    yield distance / DIAL_MAX;
                } else if (currentPosition <= distance) {
                    yield 1 + (distance - currentPosition) / DIAL_MAX;
                } else {
                    yield 0;
                }
            }
        };
    }

    /**
     * Rotates the dial by a specified distance in the given direction.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @param distance Number of positions to rotate
     * @return New dial position after rotation
     */
    int rotateDial(final int currentPosition, final Direction direction, final int distance) {
        return switch (direction) {
            case LEFT -> {
                int newPosition = (currentPosition - distance) % DIAL_MAX;
                yield newPosition < 0 ? newPosition + DIAL_MAX : newPosition;
            }
            case RIGHT -> (currentPosition + distance) % DIAL_MAX;
        };
    }

    /**
     * Rotates the dial by one position in the given direction.
     *
     * @param currentPosition Current dial position (0-99)
     * @param direction Rotation direction
     * @return New dial position after one-step rotation
     */
    int rotateDialByOne(final int currentPosition, final Direction direction) {
        return rotateDial(currentPosition, direction, 1);
    }
}
