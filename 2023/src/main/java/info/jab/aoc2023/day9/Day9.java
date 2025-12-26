package info.jab.aoc2023.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/9
 *
 * https://nickymeuleman.netlify.app/blog/aoc2023-day09/
 */
public class Day9 implements Day<Integer> {

    // Método para calcular las diferencias entre elementos consecutivos
    public List<Integer> differences(List<Integer> nums) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < nums.size() - 1; i++) {
            result.add(nums.get(i + 1) - nums.get(i));
        }
        return result;
    }

    // Método recursivo para calcular el siguiente número según la lógica dada
    public int nextNum(List<Integer> nums) {
        // Verificar si todos los números son cero
        boolean allZeros = nums.stream().allMatch(n -> n == 0);
        if (allZeros) {
            return 0;
        }

        // Calcular las diferencias
        List<Integer> differences = differences(nums);

        // Llamada recursiva con las diferencias y suma con el último elemento
        return nextNum(differences) + nums.get(nums.size() - 1);
    }

    @Override
    // Método para procesar la entrada y calcular el resultado
    public Integer getPart1Result(String fileName) {
        return Arrays.stream(fileName.split("\n"))
                .mapToInt(line -> nextNum(parseNums(line)))
                .sum();
    }

    // Método auxiliar para convertir una línea de entrada en una lista de números
    public List<Integer> parseNums(String line) {
        return Arrays.stream(line.split("\\s+"))
                .map(Integer::parseInt)
                .toList();
    }

    @Override
    public Integer getPart2Result(String fileName) {
        throw new UnsupportedOperationException();
    }
}

