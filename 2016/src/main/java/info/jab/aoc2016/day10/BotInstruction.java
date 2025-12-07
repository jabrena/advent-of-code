package info.jab.aoc2016.day10;

/**
 * Represents an instruction for a bot, specifying where to send low and high chips.
 */
public record BotInstruction(
    boolean lowIsBot,
    int lowTarget,
    boolean highIsBot,
    int highTarget
) {
}

