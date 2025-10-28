package com.example.de1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    ListView lvResults;
    Button btnBack, btnSend;
    List<Post> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mapping();
        // Nhận dữ liệu từ file
        list = readFromObjFile(this);
        if (list == null) list = new ArrayList<>();

        // Hiển thị từng dòng <diễn đàn><sở thích><lời nhắn>
        ArrayList<String> lines = new ArrayList<>();
        for (Post p : list) {
            lines.add(p.getForum() + " - " + p.getHobbies() + " - " + p.getMessage());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, lines);
        lvResults.setAdapter(adapter);

        btnBack.setOnClickListener(v -> {finish();});

        btnSend.setOnClickListener(v -> {
            if (list.isEmpty()) {
                Toast.makeText(this, "Không có đăng ký để gửi!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (Post p : list) {
                sb.append(p.getForum()).append(" - ").append(p.getHobbies())
                        .append(" - ").append(p.getMessage()).append("\n");
            }

            // Mở Gmail / email client
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:")); // chỉ app mail
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ktpm2@haui.edu.vn"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "đăng ký kết bạn");
            emailIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());

            try {
                startActivity(Intent.createChooser(emailIntent, "Gửi qua Gmail..."));
            } catch (Exception ex) {
                Toast.makeText(this, "Không có ứng dụng email nào để gửi.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public  void mapping() {
        lvResults = findViewById(R.id.lvResults);
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
    }

    public static List<Post> readFromObjFile(Context context) {
        try (FileInputStream fis = context.openFileInput(MyConstant.OBJECT_FILENAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            return (List<Post>) ois.readObject();   // chỉ đọc 1 lần

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // nếu lỗi thì trả về list rỗng
        }
    }

}