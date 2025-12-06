package info.jab.aoc2015.day22;

enum Spell {
    MAGIC_MISSILE(53, null),
    DRAIN(73, null),
    SHIELD(113, Effect.SHIELD),
    POISON(173, Effect.POISON),
    RECHARGE(229, Effect.RECHARGE);

    final int cost;
    final Effect effect;

    Spell(int cost, Effect effect) {
        this.cost = cost;
        this.effect = effect;
    }
}

