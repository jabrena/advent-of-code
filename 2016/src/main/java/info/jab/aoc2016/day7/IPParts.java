package info.jab.aoc2016.day7;

import java.util.List;

/**
 * Represents the parsed parts of an IP address.
 */
public record IPParts(List<String> supernetSequences, List<String> hypernetSequences) {
}

