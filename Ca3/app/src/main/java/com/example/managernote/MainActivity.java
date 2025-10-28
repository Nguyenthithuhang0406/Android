package com.example.managernote;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvDisplay;
    List<Note> lstNotes = new ArrayList<>();
    String fileName = "myData.txt";

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

        tvDisplay = findViewById(R.id.tv_display);

        // Load file hoặc giả lập dữ liệu
        loadData();
        displayNotes();
    }

    // Gắn menu vào Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_option_menu, menu);
        return true;
    }

    // Xử lý khi chọn menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            addNoteLauncher.launch(intent);
            return true;
        } else if (id == R.id.mnuClose) {
            confirmClose();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Nhận kết quả từ AddNoteActivity
    private final ActivityResultLauncher<Intent> addNoteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Note newNote = (Note) result.getData().getSerializableExtra("newNote");
                            lstNotes.add(newNote);
                            displayNotes();
                        }
                    });

    // Hiển thị danh sách ghi chú
    private void displayNotes() {
        StringBuilder builder = new StringBuilder();
        for (Note n : lstNotes) builder.append(n.toString());
        tvDisplay.setText(builder.toString());
    }

    // Giả lập dữ liệu ban đầu hoặc đọc từ file
    private void loadData() {
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(openFileInput(fileName))) {
                lstNotes = (List<Note>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lstNotes.add(new Note("Ôn thi Android", "Làm lại các dạng bài thầy cho"));
            lstNotes.add(new Note("Triển khai bài tập lớn", "Thống nhất quy chế làm việc"));
        }
    }

    // Lưu file trước khi thoát
    private void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(openFileOutput(fileName, MODE_PRIVATE))) {
            oos.writeObject(lstNotes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hộp thoại xác nhận khi đóng
    private void confirmClose() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn lưu và thoát không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    saveData();
                    finish();
                })
                .setNegativeButton("Không", (dialog, which) -> finish())
                .show();
    }

}