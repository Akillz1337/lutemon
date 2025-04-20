package com.example.lutemon;
public class BattleField extends Storage {
    private static BattleField instance = null;

    private BattleField() {
        super("Battle Field");
    }

    public static BattleField getInstance() {
        if (instance == null) {
            instance = new BattleField();
        }
        return instance;
    }

    public String fight(int lutemonId1, int lutemonId2) {
        Lutemon lutemon1 = getLutemon(lutemonId1);
        Lutemon lutemon2 = getLutemon(lutemonId2);

        if (lutemon1 == null || lutemon2 == null) {
            return "Invalid Lutemon selection!";
        }

        StringBuilder battleLog = new StringBuilder();
        battleLog.append(lutemon1.toString()).append("\n");
        battleLog.append(lutemon2.toString()).append("\n\n");

        Lutemon attacker = lutemon1;
        Lutemon defender = lutemon2;

        while (true) {
            battleLog.append(attacker.getName()).append(" attacks ").append(defender.getName()).append("\n");

            // Calculate attack power
            int attackPower = attacker.basicAttack();

            // Apply defense
            defender.defense(attackPower);

            if (defender.getHealth() <= 0) {
                battleLog.append(defender.getName()).append(" gets killed.\n");
                battleLog.append("The battle is over.\n");
                attacker.addExperience();
                removeLutemon(defender.getId());
                return battleLog.toString();
            }

            battleLog.append(defender.getName()).append(" manages to escape death.\n");
            battleLog.append(defender.toString()).append("\n");
            battleLog.append(attacker.toString()).append("\n\n");

            // Swap roles
            Lutemon temp = attacker;
            attacker = defender;
            defender = temp;
        }
    }
}
