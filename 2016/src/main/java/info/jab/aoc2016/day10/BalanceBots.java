package info.jab.aoc2016.day10;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public final class BalanceBots implements Solver<Integer> {

    private static final Pattern VALUE_PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    private static final Pattern BOT_PATTERN = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");
    private static final int COMPARE_VALUE_1 = 61;
    private static final int COMPARE_VALUE_2 = 17;

    @Override
    public Integer solvePartOne(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return simulateFactory(lines, true);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        var lines = ResourceLines.list(fileName);
        return simulateFactory(lines, false);
    }

    private Integer simulateFactory(final List<String> lines, final boolean findComparingBot) {
        final Map<Integer, Bot> bots = new HashMap<>();
        final Map<Integer, Integer> outputs = new HashMap<>();

        final List<String> instructions = parseInitialValues(lines, bots);
        final Map<Integer, BotInstruction> botInstructions = parseBotInstructions(instructions);

        return processBots(bots, outputs, botInstructions, findComparingBot)
            .orElseGet(() -> calculatePartTwoResult(outputs, findComparingBot));
    }

    private Integer calculatePartTwoResult(final Map<Integer, Integer> outputs, final boolean findComparingBot) {
        if (findComparingBot) {
            return -1; // Not found
        }
        // For part 2, multiply values in outputs 0, 1, and 2
        return Stream.of(0, 1, 2)
            .mapToInt(id -> outputs.getOrDefault(id, 1))
            .reduce(1, (a, b) -> a * b);
    }

    private List<String> parseInitialValues(final List<String> lines, final Map<Integer, Bot> bots) {
        return lines.stream()
            .filter(line -> {
                final Matcher valueMatcher = VALUE_PATTERN.matcher(line);
                if (valueMatcher.matches()) {
                    final int value = Integer.parseInt(valueMatcher.group(1));
                    final int botId = Integer.parseInt(valueMatcher.group(2));
                    bots.computeIfAbsent(botId, Bot::new).addChip(value);
                    return false; // Exclude from instructions
                }
                return true; // Include in instructions
            })
            .toList();
    }

    private Map<Integer, BotInstruction> parseBotInstructions(final List<String> instructions) {
        return instructions.stream()
            .map(BOT_PATTERN::matcher)
            .filter(Matcher::matches)
            .collect(Collectors.toUnmodifiableMap(
                matcher -> Integer.parseInt(matcher.group(1)),
                this::createBotInstruction
            ));
    }

    private BotInstruction createBotInstruction(final Matcher matcher) {
        final String lowType = matcher.group(2);
        final int lowId = Integer.parseInt(matcher.group(3));
        final String highType = matcher.group(4);
        final int highId = Integer.parseInt(matcher.group(5));

        return new BotInstruction(
            "bot".equals(lowType), lowId,
            "bot".equals(highType), highId
        );
    }

    private Optional<Integer> processBots(
            final Map<Integer, Bot> bots,
            final Map<Integer, Integer> outputs,
            final Map<Integer, BotInstruction> botInstructions,
            final boolean findComparingBot) {

        while (true) {
            final List<Bot> readyBots = getReadyBots(bots);

            if (readyBots.isEmpty()) {
                return Optional.empty();
            }

            final Optional<Integer> comparingBotId = readyBots.stream()
                .filter(bot -> findComparingBot && bot.isComparing(COMPARE_VALUE_1, COMPARE_VALUE_2))
                .map(Bot::getId)
                .findFirst();

            if (comparingBotId.isPresent()) {
                return comparingBotId;
            }

            readyBots.forEach(bot ->
                Optional.ofNullable(botInstructions.get(bot.getId()))
                    .ifPresent(instruction -> {
                        processBotInstruction(bot, instruction, bots, outputs);
                        bot.clearChips();
                    })
            );
        }
    }

    private List<Bot> getReadyBots(final Map<Integer, Bot> bots) {
        return bots.values().stream()
            .filter(Bot::hasTwo)
            .toList();
    }

    private void processBotInstruction(
            final Bot bot,
            final BotInstruction instruction,
            final Map<Integer, Bot> bots,
            final Map<Integer, Integer> outputs) {

        final int low = bot.getLowChip();
        final int high = bot.getHighChip();

        distributeChip(low, instruction.lowIsBot(), instruction.lowTarget(), bots, outputs);
        distributeChip(high, instruction.highIsBot(), instruction.highTarget(), bots, outputs);
    }

    private void distributeChip(
            final int chipValue,
            final boolean isBot,
            final int target,
            final Map<Integer, Bot> bots,
            final Map<Integer, Integer> outputs) {

        if (isBot) {
            bots.computeIfAbsent(target, Bot::new).addChip(chipValue);
        } else {
            outputs.put(target, chipValue);
        }
    }

}
