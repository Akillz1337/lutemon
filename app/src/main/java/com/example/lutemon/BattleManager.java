package com.example.lutemon;

public class BattleManager {
    private static final int SKILL_BASIC_ATTACK = 1;
    private static final int SKILL_DEFEND = 2;
    private static final int SKILL_POWER_UP = 3;

    private Lutemon playerLutemon;
    private Enemy enemyLutemon;
    private boolean isPlayerTurn = true;
    private StringBuilder battleLog = new StringBuilder();
    private boolean battleEnded = false;

    public BattleManager(Lutemon playerLutemon, Enemy enemyLutemon) {
        this.playerLutemon = playerLutemon;
        this.enemyLutemon = enemyLutemon;

        // Record battle participation
        playerLutemon.recordBattleParticipation();

        battleLog.append("Battle started: ").append(playerLutemon.getName())
                .append(" vs ").append(enemyLutemon.getName()).append("\n\n");
    }

    // Player uses a skill
    public void usePlayerSkill(int skillId) {
        if (!isPlayerTurn || battleEnded) return;

        battleLog.append("Player's turn:\n");
        processSkill(playerLutemon, enemyLutemon, skillId);

        // Check if battle ended
        if (enemyLutemon.getHealth() <= 0) {
            battleLog.append("\n").append(enemyLutemon.getName()).append(" is defeated!\n");
            battleLog.append(playerLutemon.getName()).append(" is victorious!\n");

            // Record stats
            playerLutemon.recordBattleVictory();
            playerLutemon.recordKill();
            playerLutemon.addExperience();

            battleEnded = true;
            return;
        }

        // Switch to enemy turn
        isPlayerTurn = false;

        // Enemy AI chooses a skill
        int enemySkill = enemyLutemon.chooseSkill(playerLutemon);
        battleLog.append("\nEnemy's turn:\n");
        processSkill(enemyLutemon, playerLutemon, enemySkill);

        // Check if battle ended
        if (playerLutemon.getHealth() <= 0) {
            battleLog.append("\n").append(playerLutemon.getName()).append(" is defeated!\n");
            battleLog.append(enemyLutemon.getName()).append(" is victorious!\n");
            battleEnded = true;
            return;
        }

        // Switch back to player turn
        isPlayerTurn = true;
    }

    private void processSkill(Lutemon attacker, Lutemon defender, int skillId) {
        switch (skillId) {
            case SKILL_BASIC_ATTACK:
                battleLog.append(attacker.getName()).append(" uses Basic Attack!\n");

                // Calculate attack with randomness
                int damage = attacker.basicAttack();

                // Store original health to calculate damage dealt
                int originalHealth = defender.getHealth();

                // Apply defense
                defender.defense(damage);

                // Calculate actual damage dealt (difference in health)
                int actualDamage = originalHealth - defender.getHealth();
                attacker.recordDamageDealt(actualDamage);

                battleLog.append(defender.getName()).append(" takes ").append(actualDamage)
                        .append(" damage! (").append(defender.getHealth())
                        .append("/").append(defender.getMaxHealth()).append(" HP)\n");
                break;

            case SKILL_DEFEND:
                battleLog.append(attacker.getName()).append(" takes a defensive stance!\n");
                attacker.defend();
                break;

            case SKILL_POWER_UP:
                battleLog.append(attacker.getName()).append(" powers up for the next attack!\n");
                attacker.powerUp();
                break;
        }
    }

    public String getBattleLog() {
        return battleLog.toString();
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isBattleEnded() {
        return battleEnded;
    }

    public Lutemon getPlayerLutemon() {
        return playerLutemon;
    }

    public Enemy getEnemyLutemon() {
        return enemyLutemon;
    }
}