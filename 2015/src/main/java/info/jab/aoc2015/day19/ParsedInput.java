package info.jab.aoc2015.day19;

import java.util.List;
import java.util.Map;

/**
 * Represents parsed input containing replacements and molecule.
 * Immutable record following functional programming principles.
 */
public record ParsedInput(Map<String, List<String>> replacements, String molecule) {}
