package com.example.lutemon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;
    private Button btnSaveData, btnLoadData;
    private TextView txtNoLutemons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        btnSaveData = findViewById(R.id.btnSaveData);
        btnLoadData = findViewById(R.id.btnLoadData);
        txtNoLutemons = findViewById(R.id.txtNoLutemons);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateLutemonList();

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager dataManager = new DataManager(HomeActivity.this);
                dataManager.saveData();
                Toast.makeText(HomeActivity.this, "Game data saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager dataManager = new DataManager(HomeActivity.this);
                dataManager.loadData();
                Toast.makeText(HomeActivity.this, "Game data loaded successfully!", Toast.LENGTH_SHORT).show();
                updateLutemonList();
            }
        });
    }

    private void updateLutemonList() {
        ArrayList<Lutemon> lutemons = Home.getInstance().getLutemons();

        if (lutemons.isEmpty()) {
            txtNoLutemons.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoLutemons.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new LutemonAdapter(this, lutemons, "home");
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("HomeActivity", "onResume called - updating lutemon list");
        updateLutemonList();
    }
}
