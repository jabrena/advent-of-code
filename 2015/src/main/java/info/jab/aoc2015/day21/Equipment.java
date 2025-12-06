package info.jab.aoc2015.day21;

/**
 * Represents a complete equipment set for an RPG character.
 * Immutable record following functional programming principles.
 */
public record Equipment(Item weapon, Item armor, Item ring1, Item ring2) {
    
    /**
     * Calculates the total cost of the equipment.
     */
    public int totalCost() {
        return weapon.cost() + armor.cost() + ring1.cost() + ring2.cost();
    }
    
    /**
     * Converts equipment to a character with calculated stats.
     */
    public Character toCharacter() {
        return new Character(
                100,
                weapon.damage() + armor.damage() + ring1.damage() + ring2.damage(),
                weapon.armor() + armor.armor() + ring1.armor() + ring2.armor()
        );
    }
}
