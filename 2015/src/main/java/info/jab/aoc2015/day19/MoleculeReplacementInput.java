package info.jab.aoc2015.day19;

import java.util.List;
import java.util.Map;

/**
 * Represents the parsed input for molecule replacement problem.
 * Contains replacement rules and the target molecule to transform.
 * Immutable record following functional programming principles.
 */
public record MoleculeReplacementInput(Map<String, List<String>> replacements, String molecule) {}
