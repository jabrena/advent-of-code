package info.jab.aoc2015.day22;

import com.putoet.resources.ResourceLines;
import info.jab.aoc.Solver;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class WizardSimulator implements Solver<Integer> {

    private static final int PLAYER_HP = 50;
    private static final int PLAYER_MANA = 500;

    @Override
    public Integer solvePartOne(String fileName) {
        var input = ResourceLines.list("/" + fileName);
        var boss = parseBoss(input);
        
        return findMinManaToWin(boss, false);
    }

    @Override
    public Integer solvePartTwo(String fileName) {
        var input = ResourceLines.list("/" + fileName);
        var boss = parseBoss(input);
        
        return findMinManaToWin(boss, true);
    }

    private Boss parseBoss(List<String> input) {
        int hp = 0;
        int damage = 0;
        for (String line : input) {
            if (line.startsWith("Hit Points: ")) {
                hp = Integer.parseInt(line.substring("Hit Points: ".length()));
            } else if (line.startsWith("Damage: ")) {
                damage = Integer.parseInt(line.substring("Damage: ".length()));
            }
        }
        return new Boss(hp, damage);
    }

    private int findMinManaToWin(Boss boss, boolean hardMode) {
        PriorityQueue<GameState> queue = new PriorityQueue<>(Comparator.comparingInt(gs -> gs.manaSpent));
        queue.offer(new GameState(PLAYER_HP, PLAYER_MANA, boss.hp, 0, new EnumMap<>(Effect.class), true));
        
        int minMana = Integer.MAX_VALUE;
        Set<String> visited = new HashSet<>(); // Memoization for repeated states
        
        while (!queue.isEmpty()) {
            GameState state = queue.poll();
            
            // Pruning: if we've already spent more mana than our best solution
            if (state.manaSpent >= minMana) {
                continue;
            }
            
            // Create state key for memoization
            String stateKey = state.playerHp + "," + state.playerMana + "," + state.bossHp + "," + 
                             state.playerTurn + "," + state.effects.toString();
            
            // Skip if we've seen this state with equal or better mana cost
            if (visited.contains(stateKey)) {
                continue;
            }
            visited.add(stateKey);
            
            // Hard mode: player loses 1 HP at start of player turn
            if (hardMode && state.playerTurn) {
                state = new GameState(state.playerHp - 1, state.playerMana, state.bossHp, 
                                    state.manaSpent, state.effects, state.playerTurn);
            }
            
            // Check if player is dead in hard mode
            if (state.playerHp > 0) {
                // Apply effects at start of turn
                state = applyEffects(state);
                
                // Check if boss is dead after effects
                if (state.bossHp <= 0) {
                    minMana = Math.min(minMana, state.manaSpent);
                } else {
                    processTurn(state, queue, boss);
                }
            }
        }
        
        return minMana;
    }
    
    private void processTurn(GameState state, PriorityQueue<GameState> queue, Boss boss) {
            
        if (state.playerTurn) {
            // Player turn - try all possible spells
            for (Spell spell : Spell.values()) {
                if (canCastSpell(state, spell)) {
                    GameState newState = castSpell(state, spell);
                    queue.offer(newState);
                }
            }
        } else {
            // Boss turn
            GameState newState = bossTurn(state, boss.damage);
            if (newState != null) {
                queue.offer(newState);
            }
        }
    }

    private GameState applyEffects(GameState state) {
        int playerHp = state.playerHp;
        int playerMana = state.playerMana;
        int bossHp = state.bossHp;
        int playerArmor = 0;
        EnumMap<Effect, Integer> newEffects = new EnumMap<>(Effect.class);
        
        for (Map.Entry<Effect, Integer> entry : state.effects.entrySet()) {
            Effect effect = entry.getKey();
            int timer = entry.getValue();
            
            switch (effect) {
                case SHIELD -> playerArmor = 7;
                case POISON -> bossHp -= 3;
                case RECHARGE -> playerMana += 101;
            }
            
            if (timer > 1) {
                newEffects.put(effect, timer - 1);
            }
        }
        
        return new GameState(playerHp, playerMana, bossHp, state.manaSpent, 
                           newEffects, state.playerTurn, playerArmor);
    }

    private boolean canCastSpell(GameState state, Spell spell) {
        if (state.playerMana < spell.cost) return false;
        
        // Can't cast effect spells that are already active
        Effect effect = spell.effect;
        return effect == null || !state.effects.containsKey(effect);
    }

    private GameState castSpell(GameState state, Spell spell) {
        int playerHp = state.playerHp;
        int playerMana = state.playerMana - spell.cost;
        int bossHp = state.bossHp;
        int manaSpent = state.manaSpent + spell.cost;
        EnumMap<Effect, Integer> effects = new EnumMap<>(state.effects);
        
        // Apply immediate spell effects
        switch (spell) {
            case MAGIC_MISSILE -> bossHp -= 4;
            case DRAIN -> {
                bossHp -= 2;
                playerHp += 2;
            }
            case SHIELD -> effects.put(Effect.SHIELD, 6);
            case POISON -> effects.put(Effect.POISON, 6);
            case RECHARGE -> effects.put(Effect.RECHARGE, 5);
        }
        
        // Check if boss is dead after spell
        if (bossHp <= 0) {
            return new GameState(playerHp, playerMana, bossHp, manaSpent, effects, false);
        }
        
        // Switch to boss turn
        return new GameState(playerHp, playerMana, bossHp, manaSpent, effects, false);
    }

    private GameState bossTurn(GameState state, int bossDamage) {
        int damage = Math.max(1, bossDamage - state.playerArmor);
        int playerHp = state.playerHp - damage;
        
        if (playerHp <= 0) {
            return null; // Player dead
        }
        
        return new GameState(playerHp, state.playerMana, state.bossHp, 
                           state.manaSpent, state.effects, true, 0);
    }

    private record Boss(int hp, int damage) {}

    private static class GameState {
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

    private enum Spell {
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

    private enum Effect {
        SHIELD, POISON, RECHARGE
    }
}