package com.example.lutemon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BattleViewActivity extends AppCompatActivity {
    private TextView txtBattleLog;
    private TextView txtLutemon1, txtLutemon2;
    private Button btnNextAttack, btnEndBattle;

    private Lutemon lutemon1, lutemon2;
    private Lutemon attacker, defender;
    private StringBuilder battleLog;
    private boolean battleOver = false;
    private ImageView imgLutemon1, imgLutemon2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_view);

        // Initialize existing views
        txtBattleLog = findViewById(R.id.txtBattleLog);
        txtLutemon1 = findViewById(R.id.txtLutemon1);
        txtLutemon2 = findViewById(R.id.txtLutemon2);
        btnNextAttack = findViewById(R.id.btnNextAttack);
        btnEndBattle = findViewById(R.id.btnEndBattle);

        // Initialize the ImageViews
        imgLutemon1 = findViewById(R.id.imgLutemon1);
        imgLutemon2 = findViewById(R.id.imgLutemon2);

        int lutemonId1 = getIntent().getIntExtra("lutemon1", -1);
        int lutemonId2 = getIntent().getIntExtra("lutemon2", -1);

        if (lutemonId1 == -1 || lutemonId2 == -1) {
            finish();
            return;
        }

        lutemon1 = BattleField.getInstance().getLutemon(lutemonId1);
        lutemon2 = BattleField.getInstance().getLutemon(lutemonId2);

        if (lutemon1 == null || lutemon2 == null) {
            finish();
            return;
        }

        // Initialize battle
        attacker = lutemon1;
        defender = lutemon2;
        battleLog = new StringBuilder();

        updateBattleUI();

        btnNextAttack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!battleOver) {
                    performAttack();
                    updateBattleUI();
                }
            }
        });

        btnEndBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateBattleUI() {
        // Update Lutemon stats display
        txtLutemon1.setText(lutemon1.toString());
        txtLutemon2.setText(lutemon2.toString());

        // Set the images
        imgLutemon1.setImageResource(lutemon1.getImageResource());
        imgLutemon2.setImageResource(lutemon2.getImageResource());

        // Update battle log
        txtBattleLog.setText(battleLog.toString());

        // Check if battle is over
        if (battleOver) {
            btnNextAttack.setEnabled(false);
            btnEndBattle.setText("Return to Arena");
        }
    }

    private void performAttack() {
        battleLog.append(attacker.getName() + " attacks " + defender.getName() + "\n");

        // Calculate attack power
        int attackPower = attacker.basicAttack();

        // Apply defense
        defender.defense(attackPower);

        if (defender.getHealth() <= 0) {
            battleLog.append(defender.getName() + " gets killed.\n");
            battleLog.append("The battle is over.\n");

            // Award experience to winner
            attacker.addExperience();

            // Remove the defeated Lutemon
            BattleField.getInstance().removeLutemon(defender.getId());

            battleOver = true;
        } else {
            battleLog.append(defender.getName() + " manages to escape death.\n");
            battleLog.append(defender.toString() + "\n");
            battleLog.append(attacker.toString() + "\n\n");

            // Swap roles for next turn
            Lutemon temp = attacker;
            attacker = defender;
            defender = temp;
        }
    }
}