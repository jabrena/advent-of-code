package jab.aoc.day7;

import static jab.aoc.Utils.SPACE_SEPARATOR_PATTERN;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

/**
 * Recreate the FileSystem using a HashMap
 *
 * In order to have "Memory with the path" the solution use the help of a Stack (LIFO)
 * https://docs.oracle.com/javase/7/docs/api/java/util/Stack.html
 *
 */
class FileSystemRecreation {

    public static Function<List<String>, Map<String, Long>> from = data -> {
        Map<String, Long> fsAsMap = new HashMap<>();
        var paths = new Stack<String>();
        for (var line : data) {
            //Case: Empty
            if (line.isEmpty()) {
                continue;
            }
            //Case $ cd
            if (line.startsWith("$ cd")) {
                if (line.equals("$ cd ..")) {
                    paths.pop();
                    continue;
                }
                var dirName = SPACE_SEPARATOR_PATTERN.split(line)[2];
                paths.add(paths.isEmpty() ? dirName : paths.peek() + dirName + "/");
                continue;
            }
            //Case: $ ls
            if (line.equals("$ ls")) {
                continue;
            }
            //Case: dir
            if (line.startsWith("dir")) {
                continue;
            }
            //Case: file
            long weight = Long.parseLong(SPACE_SEPARATOR_PATTERN.split(line)[0]);
            paths.forEach(p -> fsAsMap.put(p, fsAsMap.getOrDefault(p, 0L) + weight));
        }

        return fsAsMap;
    };
}
