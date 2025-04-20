package com.example.lutemon;

public class Enemy extends Lutemon {
    private int aiLevel; // 1 = easy, 2 = medium, 3 = hard

    public Enemy(String name, String color, int attack, int defense, int maxHealth, int aiLevel) {
        super(name, color, attack, defense, maxHealth);
        this.aiLevel = aiLevel;
    }

    // AI determines which skill to use
    public int chooseSkill(Lutemon opponent) {
        // Simple AI logic
        if (aiLevel == 1) {
            return 1; // Easy AI always uses basic attack
        } else if (aiLevel == 2) {
            // Medium AI: 70% basic attack, 15% defend, 15% power up
            int random = (int)(Math.random() * 100);
            if (random < 70) return 1;
            else if (random < 85) return 2;
            else return 3;
        } else {
            // Hard AI: Makes smarter decisions
            if (getHealth() < getMaxHealth() * 0.3) {
                // Low health - higher chance to defend
                return (Math.random() < 0.6) ? 2 : 1;
            } else if (opponent.getHealth() < opponent.getMaxHealth() * 0.3) {
                // Opponent low health - higher chance to power up or attack
                return (Math.random() < 0.4) ? 3 : 1;
            } else {
                // Normal situation - balanced approach
                int random = (int)(Math.random() * 100);
                if (random < 50) return 1;
                else if (random < 75) return 2;
                else return 3;
            }
        }
    }
}
