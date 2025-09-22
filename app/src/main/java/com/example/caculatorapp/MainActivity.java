package com.example.caculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView a, b, result;
    Button btnPlus, btnMinus, btnMultiply, btnDivide;
    String phepToan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initView();
        setupBtn();
    }

    private void initView() {
        a = findViewById(R.id.inputA);
        b = findViewById(R.id.inputB);
        btnPlus = findViewById(R.id.btnPlus);
        btnMinus = findViewById(R.id.btnMinus);
        btnMultiply = findViewById(R.id.btnMultiply);
        btnDivide = findViewById(R.id.btnDivide);
        result = findViewById(R.id.result);
    }

    private void setupBtn(){
        btnPlus.setOnClickListener(v -> {
            phepToan = "+";
            if(!validateInput()) return;
            result.setText(noiChuoi(a.getText().toString(), b.getText().toString(), phepToan));
        });

        btnMinus.setOnClickListener(v -> {
            phepToan = "-";
            if(!validateInput()) return;
            result.setText(noiChuoi(a.getText().toString(), b.getText().toString(), phepToan));
        });

        btnMultiply.setOnClickListener(v -> {
            phepToan = "*";
            if(!validateInput()) return;
            result.setText(noiChuoi(a.getText().toString(), b.getText().toString(), phepToan));
        });

        btnDivide.setOnClickListener(v -> {
            phepToan = "/";
            if(!validateInput()) return;
            result.setText(noiChuoi(a.getText().toString(), b.getText().toString(), phepToan));
        });
    }

    private String noiChuoi(String a, String b , String phepToan){
        Integer res = 0;
        if(phepToan.equals("+")){
            res = Integer.parseInt(a) + Integer.parseInt(b);
        }
        else if(phepToan.equals("-")){
            res = Integer.parseInt(a) - Integer.parseInt(b);
        }
        else if(phepToan.equals("*")){
            res = Integer.parseInt(a) * Integer.parseInt(b);
        }
        else if(phepToan.equals("/")){
            res = Integer.parseInt(a) / Integer.parseInt(b);
        }
        return a + phepToan + b + "=" + res;
    }

    private boolean validateInput(){
        if(a.getText().toString().isEmpty()){
            showToast("Số A không được để trống");
            return false;
        }
        if(b.getText().toString().isEmpty()){
            showToast("Số B không được để trống");
            return false;
        }
        return true;
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}

