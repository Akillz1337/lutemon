package com.example.lutemon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BattleSystemActivity extends AppCompatActivity {
    private TextView txtBattleLog;
    private TextView txtPlayerStats, txtEnemyStats;
    private Button btnAttack, btnDefend, btnPowerUp;
    private Button btnEndBattle;
    private ImageView imgPlayerLutemon, imgEnemyLutemon;
    private TextView txtBattleTitle;

    private BattleManager battleManager;
    private Lutemon playerLutemon;
    private Enemy enemyLutemon;

    // Instead of static instances, we'll define the enemy templates here
    private static final String[][] ENEMY_TEMPLATES = {
            // name, color, attack, defense, maxHealth, aiLevel
            {"Goblin", "Green", "5", "2", "15", "1"},  // Easy enemy
            {"Troll", "Black", "8", "3", "25", "2"},   // Medium enemy
            {"Dragon", "Red", "12", "5", "35", "3"}    // Hard enemy
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_battle_system);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading battle UI: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            // Initialize UI components
            txtBattleLog = findViewById(R.id.txtBattleLog);
            txtPlayerStats = findViewById(R.id.txtPlayerStats);
            txtEnemyStats = findViewById(R.id.txtEnemyStats);
            txtBattleTitle = findViewById(R.id.txtBattleTitle);

            // Initialize image views
            imgPlayerLutemon = findViewById(R.id.imgPlayerLutemon);
            imgEnemyLutemon = findViewById(R.id.imgEnemyLutemon);

            btnAttack = findViewById(R.id.btnAttack);
            btnDefend = findViewById(R.id.btnDefend);
            btnPowerUp = findViewById(R.id.btnPowerUp);
            btnEndBattle = findViewById(R.id.btnEndBattle);

            // Get player's Lutemon
            int lutemonId = getIntent().getIntExtra("lutemon_id", -1);
            if (lutemonId == -1) {
                Toast.makeText(this, "Error: No Lutemon selected", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            playerLutemon = BattleField.getInstance().getLutemon(lutemonId);
            if (playerLutemon == null) {
                Toast.makeText(this, "Error: Could not find selected Lutemon", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Get difficulty level (defaults to 0/easy)
            int difficultyLevel = getIntent().getIntExtra("difficulty", 0);

            // Create a new enemy instance based on the template
            if (difficultyLevel >= 0 && difficultyLevel < ENEMY_TEMPLATES.length) {
                String[] template = ENEMY_TEMPLATES[difficultyLevel];
                enemyLutemon = createEnemy(template[0], template[1],
                        Integer.parseInt(template[2]),
                        Integer.parseInt(template[3]),
                        Integer.parseInt(template[4]),
                        Integer.parseInt(template[5]));
            } else {
                // Default to easy enemy if difficulty is invalid
                String[] template = ENEMY_TEMPLATES[0];
                enemyLutemon = createEnemy(template[0], template[1],
                        Integer.parseInt(template[2]),
                        Integer.parseInt(template[3]),
                        Integer.parseInt(template[4]),
                        Integer.parseInt(template[5]));
            }

            // Initialize battle manager
            battleManager = new BattleManager(playerLutemon, enemyLutemon);

            // Set up UI
            updateUI();

            // Set up button listeners
            btnAttack.setOnClickListener(v -> {
                if (battleManager.isPlayerTurn() && !battleManager.isBattleEnded()) {
                    battleManager.usePlayerSkill(1); // Basic attack
                    updateUI();
                }
            });

            btnDefend.setOnClickListener(v -> {
                if (battleManager.isPlayerTurn() && !battleManager.isBattleEnded()) {
                    battleManager.usePlayerSkill(2); // Defend
                    updateUI();
                }
            });

            btnPowerUp.setOnClickListener(v -> {
                if (battleManager.isPlayerTurn() && !battleManager.isBattleEnded()) {
                    battleManager.usePlayerSkill(3); // Power up
                    updateUI();
                }
            });

            btnEndBattle.setOnClickListener(v -> {
                // Return player Lutemon to Home with full health
                playerLutemon.heal();
                Home.getInstance().addLutemon(playerLutemon);
                BattleField.getInstance().removeLutemon(playerLutemon.getId());
                finish();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing battle: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // Create a fresh enemy instance each time
    private Enemy createEnemy(String name, String color, int attack, int defense, int maxHealth, int aiLevel) {
        return new Enemy(name, color, attack, defense, maxHealth, aiLevel);
    }

    private void updateUI() {
        try {
            // Update stats display
            txtPlayerStats.setText(playerLutemon.toString());
            txtEnemyStats.setText(enemyLutemon.toString());

            // Set the battle title
            txtBattleTitle.setText("BATTLE: " + playerLutemon.getName() + " VS " + enemyLutemon.getName());

            // Update battle log
            txtBattleLog.setText(battleManager.getBattleLog());

            // Set images
            try {
                imgPlayerLutemon.setImageResource(playerLutemon.getImageResource());

                // Try to use enemy image or fallback to default
                try {
                    imgEnemyLutemon.setImageResource(R.drawable.enemy_lutemon);
                } catch (Exception e) {
                    // If enemy image doesn't exist, try to use a color-appropriate image
                    switch(enemyLutemon.getColor().toLowerCase()) {
                        case "green":
                            imgEnemyLutemon.setImageResource(R.drawable.lutemon_green);
                            break;
                        case "black":
                            imgEnemyLutemon.setImageResource(R.drawable.lutemon_black);
                            break;
                        case "red":
                        default:
                            // Try using a system resource as last resort
                            imgEnemyLutemon.setImageResource(android.R.drawable.ic_dialog_alert);
                            break;
                    }
                }
            } catch (Exception e) {
                // Images aren't critical, continue without them
            }

            // Scroll battle log to bottom
            final ScrollView scrollView = findViewById(R.id.scrollBattleLog);
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));

            // Enable/disable buttons based on battle state
            boolean buttonsEnabled = battleManager.isPlayerTurn() && !battleManager.isBattleEnded();
            btnAttack.setEnabled(buttonsEnabled);
            btnDefend.setEnabled(buttonsEnabled);
            btnPowerUp.setEnabled(buttonsEnabled);

            // Update end battle button text if battle ended
            if (battleManager.isBattleEnded()) {
                btnEndBattle.setText("Return to Home");
                btnEndBattle.setEnabled(true);
            } else {
                btnEndBattle.setText("Forfeit Battle");
            }

            // Apply visual effects for active skills
            if (playerLutemon.isPowerUpActive()) {
                btnAttack.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                btnAttack.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
            }

            if (playerLutemon.isDefenseActive()) {
                txtPlayerStats.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                txtPlayerStats.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error updating UI: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}