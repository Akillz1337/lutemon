package com.example.lutemon;
public abstract class Lutemon {
    private String name;
    private String color;
    protected int attack;
    protected int defense;
    protected int experience;
    protected int health;
    protected int maxHealth;
    private int id;
    private boolean powerUpActive = false;
    private boolean defenseActive = false;
    private static int idCounter = 0;

    // Stats tracking
    private int battlesParticipated = 0;
    private int battlesWon = 0;
    private int trainingDays = 0;
    private int damageDealt = 0;
    private int damageTaken = 0;
    private int kills = 0;

    public Lutemon(String name, String color, int attack, int defense, int maxHealth) {
        this.name = name;
        this.color = color;
        this.attack = attack;
        this.defense = defense;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.experience = 0;

        this.id = idCounter++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

    // Lutemon image display
    public int getImageResource() {
        switch(color.toLowerCase()) {
            case "white": return R.drawable.lutemon_white;
            case "green": return R.drawable.lutemon_green;
            case "pink": return R.drawable.lutemon_pink;
            case "orange": return R.drawable.lutemon_orange;
            case "black": return R.drawable.lutemon_black;
            default: return R.drawable.lutemon_default;
        }
    }

    public void reset() {
        this.health = this.maxHealth;
        this.experience = 0; // Reset experience
    }

    public String getColor() {
        return color;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getExperience() {
        return experience;
    }

    public void addExperience() {
        this.experience++;

        // Use experience to randomly upgrade a stat
        int upgradeType = (int) (Math.random() * 3); // 0, 1, or 2

        switch (upgradeType) {
            case 0: // Attack upgrade
                this.attack += 1;
                break;
            case 1: // Defense upgrade
                this.defense += 1;
                break;
            case 2: // Max health upgrade
                this.maxHealth += 2;
                this.health += 2; // Also increase current health
                break;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void heal() {
        this.health = this.maxHealth;
    }

    public int getId() {
        return id;
    }

    public static int getNumberOfCreatedLutemons() {
        return idCounter;
    }

    public int attack() {
        // Add randomness to attack
        double randomBonus = Math.random() * 3; // Random number between 0-3
        return (int)(getAttack() + randomBonus);
    }

    // Add these methods to Lutemon.java
    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPowerUpActive() {
        return powerUpActive;
    }

    public boolean isDefenseActive() {
        return defenseActive;
    }

    public void resetSkillStates() {
        powerUpActive = false;
        defenseActive = false;
    }

    public int basicAttack() {
        int attackPower = attack();
        if (powerUpActive) {
            attackPower *= 2;
            powerUpActive = false; // Power up is consumed
        }
        return attackPower;
    }

    public void defend() {
        defenseActive = true;
    }

    // Method for power up (skill 3)
    public void powerUp() {
        powerUpActive = true;
    }

    public Lutemon defense(int attackPower) {
        int damage;

        if (defenseActive) {
            // Special defense blocks up to 2x defense value
            int specialDefenseValue = getDefense() * 2;
            damage = Math.max(0, attackPower - specialDefenseValue);
            defenseActive = false; // Defense is consumed
        } else {
            // Normal defense
            damage = Math.max(0, attackPower - getDefense());
        }

        this.health -= damage;
        // Track damage taken
        this.damageTaken += damage;

        return this;
    }

    // Stats tracking methods
    public void recordBattleParticipation() {
        this.battlesParticipated++;
    }

    public void recordBattleVictory() {
        this.battlesWon++;
    }

    public void recordTrainingDay() {
        this.trainingDays++;
    }

    public void recordDamageDealt(int damage) {
        this.damageDealt += damage;
    }

    public void recordKill() {
        this.kills++;
    }

    // Getters for stats
    public int getBattlesParticipated() {
        return battlesParticipated;
    }

    public int getBattlesWon() {
        return battlesWon;
    }

    public int getTrainingDays() {
        return trainingDays;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public int getKills() {
        return kills;
    }

    // For serialization
    public void setBattlesParticipated(int battlesParticipated) {
        this.battlesParticipated = battlesParticipated;
    }

    public void setBattlesWon(int battlesWon) {
        this.battlesWon = battlesWon;
    }

    public void setTrainingDays(int trainingDays) {
        this.trainingDays = trainingDays;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    // Stats string for display
    public String getStatsString() {
        return "Battles: " + battlesParticipated +
                " | Victories: " + battlesWon +
                " | Training: " + trainingDays +
                " | Kills: " + kills;
    }

    @Override
    public String toString() {
        return id + ": " + color + "(" + name + ") att: " + getAttack() +
                "; def: " + defense + "; exp: " + experience + "; health: " +
                health + "/" + maxHealth;
    }
}