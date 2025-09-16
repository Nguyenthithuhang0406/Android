package com.example.changelanguages;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ImageView imgFlag;
    private TextView tvLoiChao;
    private Button btnVn, btnNga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        btnNga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFlag.setImageResource(R.drawable.nga);
                tvLoiChao.setText(R.string.tv_loichao_nga);
            }
        });

        btnVn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgFlag.setImageResource(R.drawable.vn);
                tvLoiChao.setText(R.string.tv_loichao_vn);
            }
        });
    }

    private void mapping(){
        imgFlag = findViewById(R.id.imgFlag);
        tvLoiChao = findViewById(R.id.tv_loichao);
        btnVn = findViewById(R.id.btn_vn);
        btnNga = findViewById(R.id.btn_nga);
    }
}