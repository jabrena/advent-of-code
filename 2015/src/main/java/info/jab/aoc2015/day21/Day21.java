package info.jab.aoc2015.day21;

import info.jab.aoc.Day;
import com.putoet.resources.ResourceLines;

import java.util.List;

public class Day21 implements Day<Integer> {

    record Item(String name, int cost, int damage, int armor) {}
    record Character(int hitPoints, int damage, int armor) {}

    private static final List<Item> WEAPONS = List.of(
        new Item("Dagger", 8, 4, 0),
        new Item("Shortsword", 10, 5, 0),
        new Item("Warhammer", 25, 6, 0),
        new Item("Longsword", 40, 7, 0),
        new Item("Greataxe", 74, 8, 0)
    );

    private static final List<Item> ARMOR = List.of(
        new Item("None", 0, 0, 0), // No armor option
        new Item("Leather", 13, 0, 1),
        new Item("Chainmail", 31, 0, 2),
        new Item("Splintmail", 53, 0, 3),
        new Item("Bandedmail", 75, 0, 4),
        new Item("Platemail", 102, 0, 5)
    );

    private static final List<Item> RINGS = List.of(
        new Item("None1", 0, 0, 0), // No ring option 1
        new Item("None2", 0, 0, 0), // No ring option 2
        new Item("Damage +1", 25, 1, 0),
        new Item("Damage +2", 50, 2, 0),
        new Item("Damage +3", 100, 3, 0),
        new Item("Defense +1", 20, 0, 1),
        new Item("Defense +2", 40, 0, 2),
        new Item("Defense +3", 80, 0, 3)
    );

    @Override
    public Integer getPart1Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        var boss = parseBoss(lines);
        
        int minCost = Integer.MAX_VALUE;
        
        // Try all combinations of equipment
        for (Item weapon : WEAPONS) {
            for (Item armor : ARMOR) {
                for (int i = 0; i < RINGS.size(); i++) {
                    for (int j = i + 1; j < RINGS.size(); j++) {
                        Item ring1 = RINGS.get(i);
                        Item ring2 = RINGS.get(j);
                        
                        // Skip if both rings are "None" (we allow 0, 1, or 2 rings)
                        if (ring1.name.startsWith("None") && ring2.name.startsWith("None")) {
                            continue;
                        }
                        
                        // Calculate total stats and cost
                        int totalCost = weapon.cost + armor.cost + ring1.cost + ring2.cost;
                        int totalDamage = weapon.damage + armor.damage + ring1.damage + ring2.damage;
                        int totalArmor = weapon.armor + armor.armor + ring1.armor + ring2.armor;
                        
                        var player = new Character(100, totalDamage, totalArmor);
                        
                        if (playerWins(player, boss)) {
                            minCost = Math.min(minCost, totalCost);
                        }
                    }
                }
                
                // Also try with just one ring or no rings
                for (Item ring : RINGS) {
                    if (!ring.name.startsWith("None")) {
                        int totalCost = weapon.cost + armor.cost + ring.cost;
                        int totalDamage = weapon.damage + armor.damage + ring.damage;
                        int totalArmor = weapon.armor + armor.armor + ring.armor;
                        
                        var player = new Character(100, totalDamage, totalArmor);
                        
                        if (playerWins(player, boss)) {
                            minCost = Math.min(minCost, totalCost);
                        }
                    }
                }
                
                // Try with no rings
                int totalCost = weapon.cost + armor.cost;
                int totalDamage = weapon.damage + armor.damage;
                int totalArmor = weapon.armor + armor.armor;
                
                var player = new Character(100, totalDamage, totalArmor);
                
                if (playerWins(player, boss)) {
                    minCost = Math.min(minCost, totalCost);
                }
            }
        }
        
        return minCost;
    }

    @Override
    public Integer getPart2Result(String fileName) {
        var lines = ResourceLines.list(fileName);
        var boss = parseBoss(lines);
        
        int maxCost = Integer.MIN_VALUE;
        
        // Try all combinations of equipment
        for (Item weapon : WEAPONS) {
            for (Item armor : ARMOR) {
                for (int i = 0; i < RINGS.size(); i++) {
                    for (int j = i + 1; j < RINGS.size(); j++) {
                        Item ring1 = RINGS.get(i);
                        Item ring2 = RINGS.get(j);
                        
                        // Skip if both rings are "None" (we allow 0, 1, or 2 rings)
                        if (ring1.name.startsWith("None") && ring2.name.startsWith("None")) {
                            continue;
                        }
                        
                        // Calculate total stats and cost
                        int totalCost = weapon.cost + armor.cost + ring1.cost + ring2.cost;
                        int totalDamage = weapon.damage + armor.damage + ring1.damage + ring2.damage;
                        int totalArmor = weapon.armor + armor.armor + ring1.armor + ring2.armor;
                        
                        var player = new Character(100, totalDamage, totalArmor);
                        
                        if (!playerWins(player, boss)) {
                            maxCost = Math.max(maxCost, totalCost);
                        }
                    }
                }
                
                // Also try with just one ring or no rings
                for (Item ring : RINGS) {
                    if (!ring.name.startsWith("None")) {
                        int totalCost = weapon.cost + armor.cost + ring.cost;
                        int totalDamage = weapon.damage + armor.damage + ring.damage;
                        int totalArmor = weapon.armor + armor.armor + ring.armor;
                        
                        var player = new Character(100, totalDamage, totalArmor);
                        
                        if (!playerWins(player, boss)) {
                            maxCost = Math.max(maxCost, totalCost);
                        }
                    }
                }
                
                // Try with no rings
                int totalCost = weapon.cost + armor.cost;
                int totalDamage = weapon.damage + armor.damage;
                int totalArmor = weapon.armor + armor.armor;
                
                var player = new Character(100, totalDamage, totalArmor);
                
                if (!playerWins(player, boss)) {
                    maxCost = Math.max(maxCost, totalCost);
                }
            }
        }
        
        return maxCost;
    }

    private Character parseBoss(List<String> lines) {
        int hitPoints = 0, damage = 0, armor = 0;
        
        for (String line : lines) {
            if (line.startsWith("Hit Points:")) {
                hitPoints = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Damage:")) {
                damage = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Armor:")) {
                armor = Integer.parseInt(line.split(": ")[1]);
            }
        }
        
        return new Character(hitPoints, damage, armor);
    }

    private boolean playerWins(Character player, Character boss) {
        int playerHp = player.hitPoints;
        int bossHp = boss.hitPoints;
        
        while (playerHp > 0 && bossHp > 0) {
            // Player attacks first
            int playerDamage = Math.max(1, player.damage - boss.armor);
            bossHp -= playerDamage;
            
            if (bossHp <= 0) {
                return true; // Player wins
            }
            
            // Boss attacks
            int bossDamage = Math.max(1, boss.damage - player.armor);
            playerHp -= bossDamage;
        }
        
        return playerHp > 0; // Player wins if they have HP left
    }
}