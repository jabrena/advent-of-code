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
        
        NodeMapData mapData = buildNodeMap(nodes);
        if (mapData.emptyNode() == null) {
            return 0;
        }
        
        return findShortestPath(mapData);
    }


    private NodeMapData buildNodeMap(List<Node> nodes) {
        Map<String, Node> originalNodeMap = new HashMap<>();
        int maxX = 0;
        int maxY = 0;
        Node emptyNode = null;
        int goalX = 0;
        
        for (Node node : nodes) {
            originalNodeMap.put(node.x() + "," + node.y(), node);
            if (node.x() > maxX) maxX = node.x();
            if (node.y() > maxY) maxY = node.y();
            if (node.y() == 0 && node.x() > goalX) {
                goalX = node.x();
            }
            if (node.isEmpty()) {
                emptyNode = node;
            }
        }
        
        return new NodeMapData(originalNodeMap, maxX, maxY, emptyNode, goalX);
    }

    private Integer findShortestPath(NodeMapData mapData) {
        GridState initialState = new GridState(mapData.goalX(), 0, mapData.emptyNode().x(), mapData.emptyNode().y());
        
        Queue<GridState> queue = new ArrayDeque<>();
        Map<GridState, Integer> steps = new HashMap<>();
        
        queue.offer(initialState);
        steps.put(initialState, 0);
        
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        
        while (!queue.isEmpty()) {
            GridState current = queue.poll();
            int currentSteps = steps.get(current);
            
            if (current.goalX() == 0 && current.goalY() == 0) {
                return currentSteps;
            }
            
            processAdjacentStates(current, currentSteps, mapData, queue, steps, dx, dy);
        }
        
        return 0;
    }

    private void processAdjacentStates(GridState current, int currentSteps, NodeMapData mapData,
                                       Queue<GridState> queue, Map<GridState, Integer> steps,
                                       int[] dx, int[] dy) {
        Node emptyNodeOriginal = mapData.originalNodeMap().get(current.emptyX() + "," + current.emptyY());
        if (emptyNodeOriginal == null) {
            return;
        }
        int emptyAvail = emptyNodeOriginal.size();
        
        for (int i = 0; i < 4; i++) {
            int newEmptyX = current.emptyX() + dx[i];
            int newEmptyY = current.emptyY() + dy[i];
            
            if (isValidPosition(newEmptyX, newEmptyY, mapData.maxX(), mapData.maxY())) {
                processNodeMove(current, currentSteps, newEmptyX, newEmptyY, emptyAvail,
                               mapData, queue, steps);
            }
        }
    }

    private boolean isValidPosition(int x, int y, int maxX, int maxY) {
        return x >= 0 && x <= maxX && y >= 0 && y <= maxY;
    }

    private void processNodeMove(GridState current, int currentSteps, int newEmptyX, int newEmptyY,
                                 int emptyAvail, NodeMapData mapData,
                                 Queue<GridState> queue, Map<GridState, Integer> steps) {
        Node sourceNode = mapData.originalNodeMap().get(newEmptyX + "," + newEmptyY);
        if (sourceNode == null) {
            return;
        }
        
        int sourceUsed = calculateSourceUsed(newEmptyX, newEmptyY, current.goalX(), current.goalY(),
                                             mapData.originalNodeMap(), sourceNode);
        
        if (sourceUsed > 0 && emptyAvail >= sourceUsed) {
            GridState newState = calculateNewState(current, newEmptyX, newEmptyY);
            
            steps.computeIfAbsent(newState, k -> {
                queue.offer(newState);
                return currentSteps + 1;
            });
        }
    }

    private int calculateSourceUsed(int newEmptyX, int newEmptyY, int goalX, int goalY,
                                   Map<String, Node> originalNodeMap, Node sourceNode) {
        if (newEmptyX == goalX && newEmptyY == goalY) {
            Node goalNodeOriginal = originalNodeMap.get(goalX + "," + goalY);
            return goalNodeOriginal != null ? goalNodeOriginal.used() : sourceNode.used();
        }
        return sourceNode.used();
    }

    private GridState calculateNewState(GridState current, int newEmptyX, int newEmptyY) {
        int newGoalX = current.goalX();
        int newGoalY = current.goalY();
        
        if (newEmptyX == current.goalX() && newEmptyY == current.goalY()) {
            newGoalX = current.emptyX();
            newGoalY = current.emptyY();
        }
        
        return new GridState(newGoalX, newGoalY, newEmptyX, newEmptyY);
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
