package com.example.managerstudent;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingActivity extends AppCompatActivity {
    private TextView tvReques;
    private RadioButton rdred, rdgreen, rdBlue;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        Intent intent = getIntent();
        String msgRequest = intent.getStringExtra("msgRequest");
        tvReques.setText(msgRequest);
        btnSet.setOnClickListener(v -> {
            int selectedColor = 0;
            if (rdred.isChecked()) {
                selectedColor = Color.RED;
            } else {
                if (rdgreen.isChecked()) {
                    selectedColor = Color.GREEN;
                } else {
                    if (rdBlue.isChecked()) {
                        selectedColor = Color.BLUE;
                    }
                }
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedColor", selectedColor);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    public void mapping() {
        tvReques = findViewById(R.id.tv_request);
        rdred = findViewById(R.id.rd_red);
        rdgreen = findViewById(R.id.rd_green);
        rdBlue = findViewById(R.id.rd_blue);
        btnSet = findViewById(R.id.btn_set_bg);

    }
}