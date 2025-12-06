package info.jab.aoc2015.day22;

import java.util.EnumMap;

class GameState {
    final int playerHp;
    final int playerMana;
    final int bossHp;
    final int manaSpent;
    final EnumMap<Effect, Integer> effects;
    final boolean playerTurn;
    final int playerArmor;

    GameState(int playerHp, int playerMana, int bossHp, int manaSpent, 
             EnumMap<Effect, Integer> effects, boolean playerTurn) {
        this(playerHp, playerMana, bossHp, manaSpent, effects, playerTurn, 0);
    }

    GameState(int playerHp, int playerMana, int bossHp, int manaSpent, 
             EnumMap<Effect, Integer> effects, boolean playerTurn, int playerArmor) {
        this.playerHp = playerHp;
        this.playerMana = playerMana;
        this.bossHp = bossHp;
        this.manaSpent = manaSpent;
        this.effects = effects;
        this.playerTurn = playerTurn;
        this.playerArmor = playerArmor;
    }
}

