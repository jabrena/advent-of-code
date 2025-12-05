package info.jab.aoc2016.day22;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Solver for Day 22: Grid Computing
 */
public class GridComputing implements Solver<Integer> {

    private static final Pattern NODE_PATTERN = Pattern.compile(
        "/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)T\\s+(\\d+)%"
    );

    record Node(int x, int y, int size, int used, int avail) {
        boolean isEmpty() {
            return used == 0;
        }

        boolean canFit(int dataSize) {
            return avail >= dataSize;
        }
    }

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Node> nodes = parseNodes(lines);

        int viablePairs = 0;
        for (int i = 0; i < nodes.size(); i++) {
            Node nodeA = nodes.get(i);
            if (nodeA.isEmpty()) {
                continue;
            }

            for (int j = 0; j < nodes.size(); j++) {
                if (i == j) {
                    continue;
                }

                Node nodeB = nodes.get(j);
                if (nodeB.canFit(nodeA.used())) {
                    viablePairs++;
                }
            }
        }

        return viablePairs;
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        List<Node> nodes = parseNodes(lines);
        
        // Build maps for quick lookup
        Map<String, Node> originalNodeMap = new HashMap<>();
        int maxX = 0;
        int maxY = 0;
        Node emptyNode = null;
        int goalX = 0;
        
        for (Node node : nodes) {
            originalNodeMap.put(node.x + "," + node.y, node);
            if (node.x > maxX) maxX = node.x;
            if (node.y > maxY) maxY = node.y;
            if (node.y == 0 && node.x > goalX) {
                goalX = node.x;
            }
            if (node.isEmpty()) {
                emptyNode = node;
            }
        }
        
        if (emptyNode == null) {
            return 0;
        }
        
        // State: (goalX, goalY, emptyX, emptyY)
        // We want to move goal from (goalX, 0) to (0, 0)
        record State(int goalX, int goalY, int emptyX, int emptyY) {}
        
        State initialState = new State(goalX, 0, emptyNode.x(), emptyNode.y());
        
        Queue<State> queue = new ArrayDeque<>();
        Map<State, Integer> steps = new HashMap<>();
        
        queue.offer(initialState);
        steps.put(initialState, 0);
        
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        
        while (!queue.isEmpty()) {
            State current = queue.poll();
            int currentSteps = steps.get(current);
            
            // Check if we reached the target
            if (current.goalX == 0 && current.goalY == 0) {
                return currentSteps;
            }
            
            // Get current empty node capacity (it's empty, so avail = size)
            Node emptyNodeOriginal = originalNodeMap.get(current.emptyX + "," + current.emptyY);
            if (emptyNodeOriginal == null) continue;
            int emptyAvail = emptyNodeOriginal.size(); // Empty node has full capacity
            
            // Try moving the empty node to adjacent positions
            for (int i = 0; i < 4; i++) {
                int newEmptyX = current.emptyX + dx[i];
                int newEmptyY = current.emptyY + dy[i];
                
                // Check bounds and node existence
                if (newEmptyX >= 0 && newEmptyX <= maxX && newEmptyY >= 0 && newEmptyY <= maxY) {
                    Node sourceNode = originalNodeMap.get(newEmptyX + "," + newEmptyY);
                    if (sourceNode != null) {
                        // Calculate current used amount at source
                        // If source is the goal position, it has the goal data (original used)
                        // Otherwise it has its original data
                        int sourceUsed;
                        if (newEmptyX == current.goalX && newEmptyY == current.goalY) {
                            // This is the goal node, it has the goal data
                            Node goalNodeOriginal = originalNodeMap.get(current.goalX + "," + current.goalY);
                            sourceUsed = goalNodeOriginal != null ? goalNodeOriginal.used() : sourceNode.used();
                        } else {
                            // Regular node with its original data
                            sourceUsed = sourceNode.used();
                        }
                        
                        // Check if we can move data from source to empty
                        if (sourceUsed > 0 && emptyAvail >= sourceUsed) {
                            // Determine new goal position
                            int newGoalX = current.goalX;
                            int newGoalY = current.goalY;
                            
                            // If we're moving the goal node itself
                            if (newEmptyX == current.goalX && newEmptyY == current.goalY) {
                                newGoalX = current.emptyX;
                                newGoalY = current.emptyY;
                            }
                            
                            State newState = new State(newGoalX, newGoalY, newEmptyX, newEmptyY);
                            
                            if (!steps.containsKey(newState)) {
                                steps.put(newState, currentSteps + 1);
                                queue.offer(newState);
                            }
                        }
                    }
                }
            }
        }
        
        return 0;
    }

    private List<Node> parseNodes(List<String> lines) {
        List<Node> nodes = new ArrayList<>();
        for (String line : lines) {
            Matcher matcher = NODE_PATTERN.matcher(line);
            if (matcher.matches()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int size = Integer.parseInt(matcher.group(3));
                int used = Integer.parseInt(matcher.group(4));
                int avail = Integer.parseInt(matcher.group(5));
                nodes.add(new Node(x, y, size, used, avail));
            }
        }
        return nodes;
    }
}
