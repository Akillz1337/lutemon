package com.example.lutemon;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView txtHomeCount, txtTrainingCount, txtBattleCount;
    private Button btnCreateNew, btnViewHome, btnViewTraining, btnViewBattle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        txtHomeCount = findViewById(R.id.txtHomeCount);
        txtTrainingCount = findViewById(R.id.txtTrainingCount);
        txtBattleCount = findViewById(R.id.txtBattleCount);

        btnCreateNew = findViewById(R.id.btnCreateNew);
        btnViewHome = findViewById(R.id.btnViewHome);
        btnViewTraining = findViewById(R.id.btnViewTraining);
        btnViewBattle = findViewById(R.id.btnViewBattle);

        DataManager dataManager = new DataManager(this);
        boolean dataLoaded = dataManager.loadData();
        Log.d("MainActivity", "Auto-loading data onCreate: " + dataLoaded);

        updateCounts();

        // Set up button click listeners
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateLutemonActivity.class);
                startActivity(intent);
            }
        });

        btnViewHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        btnViewTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrainingActivity.class);
                startActivity(intent);
            }
        });

        btnViewBattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BattleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save data when app is sent to background
        DataManager dataManager = new DataManager(this);
        dataManager.saveData();
        Log.d("MainActivity", "Auto-saving data onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounts();
    }

    private void updateCounts() {
        txtHomeCount.setText("You have " + Home.getInstance().getLutemons().size() + " Lutemons at home");
        txtTrainingCount.setText("You have " + TrainingArea.getInstance().getLutemons().size() + " Lutemons training");
        txtBattleCount.setText("You have " + BattleField.getInstance().getLutemons().size() + " Lutemons ready for battle");
    }
}