package com.example.lutemon;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LutemonAdapter adapter;
    private TextView txtNoLutemons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        recyclerView = findViewById(R.id.recyclerView);
        txtNoLutemons = findViewById(R.id.txtNoLutemons);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateLutemonList();
    }

    private void updateLutemonList() {
        ArrayList<Lutemon> lutemons = TrainingArea.getInstance().getLutemons();

        if (lutemons.isEmpty()) {
            txtNoLutemons.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNoLutemons.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new LutemonAdapter(this, lutemons, "training");
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