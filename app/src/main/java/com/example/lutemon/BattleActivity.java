package com.example.lutemon;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BattleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;
    private TextView txtNoLutemons;
    private Button btnStartBattle;

    private int selectedLutemonId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        recyclerView = findViewById(R.id.recyclerView);
        txtNoLutemons = findViewById(R.id.txtNoLutemons);
        btnStartBattle = findViewById(R.id.btnStartBattle);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateLutemonList();

        btnStartBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLutemonId != -1) {
                    // Launch the enemy selection dialog
                    showEnemySelectionDialog();
                } else {
                    Toast.makeText(BattleActivity.this,
                            "Please select a Lutemon for battle",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showEnemySelectionDialog() {
        // Create an array of enemy names
        final String[] enemyOptions = {"Easy: Goblin", "Medium: Troll", "Hard: Dragon"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Your Opponent")
                .setItems(enemyOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Launch battle with selected difficulty
                        Intent intent = new Intent(BattleActivity.this, BattleSystemActivity.class);
                        intent.putExtra("lutemon_id", selectedLutemonId);
                        intent.putExtra("difficulty", which); // 0=easy, 1=medium, 2=hard
                        startActivity(intent);
                    }
                });
        builder.create().show();
    }

    private void updateLutemonList() {
        ArrayList<Lutemon> lutemons = BattleField.getInstance().getLutemons();

        if (lutemons.isEmpty()) {
            txtNoLutemons.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnStartBattle.setVisibility(View.GONE);
        } else {
            txtNoLutemons.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnStartBattle.setVisibility(View.VISIBLE);
            btnStartBattle.setText("Battle Against Enemy");
            btnStartBattle.setEnabled(selectedLutemonId != -1);

            adapter = new LutemonAdapter(this, lutemons, "battle");
            recyclerView.setAdapter(adapter);
        }
    }

    public void selectLutemonForBattle(int id) {
        selectedLutemonId = id;
        btnStartBattle.setEnabled(true);
    }

    public void deselectLutemon(int id) {
        if (id == -1) {
            // Special case: deselect all
            selectedLutemonId = -1;
        } else if (selectedLutemonId == id) {
            selectedLutemonId = -1;
        }
        btnStartBattle.setEnabled(selectedLutemonId != -1);
    }

    public boolean isLutemonSelected(int id) {
        return selectedLutemonId == id;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BattleActivity", "onResume called - updating lutemon list");
        updateLutemonList();
        // Reset selection when returning to this screen
        selectedLutemonId = -1;
        btnStartBattle.setEnabled(false);
    }
}