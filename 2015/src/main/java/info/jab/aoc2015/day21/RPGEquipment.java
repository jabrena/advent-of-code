package info.jab.aoc2015.day21;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Solver for RPG equipment optimization.
 * Uses functional programming principles:
 * - Pure functions for calculations
 * - Stream API for generating combinations
 * - Immutable data structures
 */
public final class RPGEquipment implements Solver<Integer> {

    record Item(String name, int cost, int damage, int armor) {}
    record Character(int hitPoints, int damage, int armor) {}
    record Equipment(Item weapon, Item armor, Item ring1, Item ring2) {
        int totalCost() {
            return weapon.cost() + armor.cost() + ring1.cost() + ring2.cost();
        }
        
        Character toCharacter() {
            return new Character(
                    100,
                    weapon.damage() + armor.damage() + ring1.damage() + ring2.damage(),
                    weapon.armor() + armor.armor() + ring1.armor() + ring2.armor()
            );
        }
    }

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
    public Integer solvePartOne(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final Character boss = parseBoss(lines);
        
        return generateAllEquipmentCombinations()
                .filter(equipment -> playerWins(equipment.toCharacter(), boss))
                .mapToInt(Equipment::totalCost)
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    @Override
    public Integer solvePartTwo(final String fileName) {
        final List<String> lines = ResourceLines.list(fileName);
        final Character boss = parseBoss(lines);
        
        return generateAllEquipmentCombinations()
                .filter(equipment -> !playerWins(equipment.toCharacter(), boss))
                .mapToInt(Equipment::totalCost)
                .max()
                .orElse(Integer.MIN_VALUE);
    }
    
    /**
     * Pure function: generates all valid equipment combinations using stream API.
     */
    private Stream<Equipment> generateAllEquipmentCombinations() {
        return WEAPONS.stream()
                .flatMap(weapon -> ARMOR.stream()
                        .flatMap(armor -> {
                            // Two rings
                            final Stream<Equipment> twoRings = IntStream.range(0, RINGS.size())
                                    .boxed()
                                    .flatMap(i -> IntStream.range(i + 1, RINGS.size())
                                            .mapToObj(j -> {
                                                final Item ring1 = RINGS.get(i);
                                                final Item ring2 = RINGS.get(j);
                                                // Skip if both rings are "None"
                                                if (ring1.name().startsWith("None") && ring2.name().startsWith("None")) {
                                                    return null;
                                                }
                                                return new Equipment(weapon, armor, ring1, ring2);
                                            })
                                            .filter(eq -> eq != null)
                                    );
                            
                            // One ring
                            final Stream<Equipment> oneRing = RINGS.stream()
                                    .filter(ring -> !ring.name().startsWith("None"))
                                    .map(ring -> new Equipment(weapon, armor, ring, new Item("None", 0, 0, 0)));
                            
                            // No rings
                            final Equipment noRings = new Equipment(
                                    weapon,
                                    armor,
                                    new Item("None", 0, 0, 0),
                                    new Item("None", 0, 0, 0)
                            );
                            
                            return Stream.concat(Stream.concat(twoRings, oneRing), Stream.of(noRings));
                        })
                );
    }

    /**
     * Pure function: parses boss stats using stream API.
     */
    private Character parseBoss(final List<String> lines) {
        final int hitPoints = lines.stream()
                .filter(line -> line.startsWith("Hit Points:"))
                .mapToInt(line -> Integer.parseInt(line.split(": ")[1]))
                .findFirst()
                .orElse(0);
        
        final int damage = lines.stream()
                .filter(line -> line.startsWith("Damage:"))
                .mapToInt(line -> Integer.parseInt(line.split(": ")[1]))
                .findFirst()
                .orElse(0);
        
        final int armor = lines.stream()
                .filter(line -> line.startsWith("Armor:"))
                .mapToInt(line -> Integer.parseInt(line.split(": ")[1]))
                .findFirst()
                .orElse(0);
        
        return new Character(hitPoints, damage, armor);
    }

    /**
     * Pure function: determines if player wins using functional recursion.
     */
    private boolean playerWins(final Character player, final Character boss) {
        return simulateBattle(player, boss);
    }
    
    /**
     * Pure recursive function: simulates battle without mutating state.
     */
    private boolean simulateBattle(final Character player, final Character boss) {
        if (boss.hitPoints() <= 0) {
            return true;
        }
        if (player.hitPoints() <= 0) {
            return false;
        }
        
        // Player attacks first
        final int playerDamage = Math.max(1, player.damage() - boss.armor());
        final Character newBoss = new Character(boss.hitPoints() - playerDamage, boss.damage(), boss.armor());
        
        if (newBoss.hitPoints() <= 0) {
            return true;
        }
        
        // Boss attacks
        final int bossDamage = Math.max(1, boss.damage() - player.armor());
        final Character newPlayer = new Character(player.hitPoints() - bossDamage, player.damage(), player.armor());
        
        return simulateBattle(newPlayer, newBoss);
    }
}
