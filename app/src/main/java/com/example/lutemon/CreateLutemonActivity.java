package com.example.lutemon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateLutemonActivity extends AppCompatActivity {
    private EditText editName;
    private RadioGroup radioGroup;
    private Button btnCreate, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lutemon);

        editName = findViewById(R.id.editName);
        radioGroup = findViewById(R.id.radioGroup);
        btnCreate = findViewById(R.id.btnCreate);
        btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(CreateLutemonActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(CreateLutemonActivity.this, "Please select a color", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton radioButton = findViewById(selectedId);
                String fullText = radioButton.getText().toString();
                String color;

                // Extract just the color name from the radio button text
                if (fullText.contains(" ")) {
                    color = fullText.substring(0, fullText.indexOf(" "));
                } else {
                    color = fullText;
                }

                // Create the Lutemon
                Home.getInstance().createLutemon(color, name);
                Toast.makeText(CreateLutemonActivity.this, "Lutemon created successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}