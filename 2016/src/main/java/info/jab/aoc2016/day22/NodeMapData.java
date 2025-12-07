package info.jab.aoc2016.day22;

import java.util.Map;

/**
 * Represents the parsed node map data.
 */
public record NodeMapData(Map<String, Node> originalNodeMap, int maxX, int maxY, Node emptyNode, int goalX) {
}

