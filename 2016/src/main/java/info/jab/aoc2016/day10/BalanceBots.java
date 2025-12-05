package info.jab.aoc2016.day10;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.putoet.resources.ResourceLines;

import info.jab.aoc.Solver;

public class BalanceBots implements Solver<Integer> {

    private static final Pattern VALUE_PATTERN = Pattern.compile("value (\\d+) goes to bot (\\d+)");
    private static final Pattern BOT_PATTERN = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)");

    @Override
    public Integer solvePartOne(String fileName) {
        var lines = ResourceLines.list(fileName);
        return simulateFactory(lines, true);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var lines = ResourceLines.list(fileName);
        return simulateFactory(lines, false);
    }

    private Integer simulateFactory(List<String> lines, boolean findComparingBot) {
        Map<Integer, Bot> bots = new HashMap<>();
        Map<Integer, Integer> outputs = new HashMap<>();
        List<String> instructions = parseInitialValues(lines, bots);
        Map<Integer, BotInstruction> botInstructions = parseBotInstructions(instructions);
        
        Integer result = processBots(bots, outputs, botInstructions, findComparingBot);
        if (result != null) {
            return result;
        }
        
        // For part 2, multiply values in outputs 0, 1, and 2
        if (!findComparingBot) {
            return outputs.getOrDefault(0, 1) * outputs.getOrDefault(1, 1) * outputs.getOrDefault(2, 1);
        }
        
        return -1; // Not found
    }

    private List<String> parseInitialValues(List<String> lines, Map<Integer, Bot> bots) {
        List<String> instructions = new ArrayList<>();
        for (String line : lines) {
            Matcher valueMatcher = VALUE_PATTERN.matcher(line);
            if (valueMatcher.matches()) {
                int value = Integer.parseInt(valueMatcher.group(1));
                int botId = Integer.parseInt(valueMatcher.group(2));
                bots.computeIfAbsent(botId, Bot::new).addChip(value);
            } else {
                instructions.add(line);
            }
        }
        return instructions;
    }

    private Map<Integer, BotInstruction> parseBotInstructions(List<String> instructions) {
        Map<Integer, BotInstruction> botInstructions = new HashMap<>();
        for (String instruction : instructions) {
            Matcher botMatcher = BOT_PATTERN.matcher(instruction);
            if (botMatcher.matches()) {
                int botId = Integer.parseInt(botMatcher.group(1));
                String lowType = botMatcher.group(2);
                int lowId = Integer.parseInt(botMatcher.group(3));
                String highType = botMatcher.group(4);
                int highId = Integer.parseInt(botMatcher.group(5));
                
                botInstructions.put(botId, new BotInstruction(
                    lowType.equals("bot"), lowId,
                    highType.equals("bot"), highId
                ));
            }
        }
        return botInstructions;
    }

    private Integer processBots(Map<Integer, Bot> bots, Map<Integer, Integer> outputs,
                                 Map<Integer, BotInstruction> botInstructions, boolean findComparingBot) {
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Bot> readyBots = getReadyBots(bots);
            
            for (Bot bot : readyBots) {
                if (findComparingBot && bot.isComparing(61, 17)) {
                    return bot.getId();
                }
                
                BotInstruction instruction = botInstructions.get(bot.getId());
                if (instruction != null) {
                    processBotInstruction(bot, instruction, bots, outputs);
                    bot.clearChips();
                    changed = true;
                }
            }
        }
        return null;
    }

    private List<Bot> getReadyBots(Map<Integer, Bot> bots) {
        List<Bot> readyBots = new ArrayList<>();
        for (Bot bot : bots.values()) {
            if (bot.hasTwo()) {
                readyBots.add(bot);
            }
        }
        return readyBots;
    }

    private void processBotInstruction(Bot bot, BotInstruction instruction,
                                       Map<Integer, Bot> bots, Map<Integer, Integer> outputs) {
        int low = bot.getLowChip();
        int high = bot.getHighChip();
        
        if (instruction.lowIsBot) {
            bots.computeIfAbsent(instruction.lowTarget, Bot::new).addChip(low);
        } else {
            outputs.put(instruction.lowTarget, low);
        }
        
        if (instruction.highIsBot) {
            bots.computeIfAbsent(instruction.highTarget, Bot::new).addChip(high);
        } else {
            outputs.put(instruction.highTarget, high);
        }
    }

    private static class Bot {
        private final int id;
        private final List<Integer> chips = new ArrayList<>();
        
        public Bot(int id) {
            this.id = id;
        }
        
        public int getId() {
            return id;
        }
        
        public void addChip(int value) {
            chips.add(value);
        }
        
        public boolean hasTwo() {
            return chips.size() == 2;
        }
        
        public boolean isComparing(int value1, int value2) {
            if (chips.size() != 2) return false;
            return (chips.contains(value1) && chips.contains(value2));
        }
        
        public int getLowChip() {
            return Math.min(chips.get(0), chips.get(1));
        }
        
        public int getHighChip() {
            return Math.max(chips.get(0), chips.get(1));
        }
        
        public void clearChips() {
            chips.clear();
        }
    }
    
    private static class BotInstruction {
        public final boolean lowIsBot;
        public final int lowTarget;
        public final boolean highIsBot;
        public final int highTarget;
        
        public BotInstruction(boolean lowIsBot, int lowTarget, boolean highIsBot, int highTarget) {
            this.lowIsBot = lowIsBot;
            this.lowTarget = lowTarget;
            this.highIsBot = highIsBot;
            this.highTarget = highTarget;
        }
    }
}