package com.example.kiemtratx2_de04;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ListView lvNotes;
    Button btnClose;
    ArrayList<Note> lstNotes;
    ArrayAdapter<Note> adapter;
    int selectedIndex = -1;
    final String FILE_NAME = "myData.txt";

    ActivityResultLauncher<Intent> editNoteLauncher;

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

        lvNotes = findViewById(R.id.lvNotes);
        btnClose = findViewById(R.id.btnClose);

        // Load dữ liệu
        lstNotes = loadFromFile();
        if (lstNotes == null || lstNotes.isEmpty()) {
            loadFakeData();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lstNotes);
        lvNotes.setAdapter(adapter);

        registerForContextMenu(lvNotes);

        // ActivityResult launcher cho EditNote
        editNoteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Note updatedNote = (Note) result.getData().getSerializableExtra("updatedNote");
                        if (selectedIndex >= 0 && updatedNote != null) {
                            lstNotes.set(selectedIndex, updatedNote);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
        );

        btnClose.setOnClickListener(v -> {
            confirmCloseApp();
        });

    }
    // Giả lập dữ liệu ban đầu
    private void loadFakeData() {
        lstNotes = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss", Locale.getDefault());
        lstNotes.add(new Note("Ôn thi Android", sdf.format(new Date()), "Học Activity, Intent và Context Menu"));
        lstNotes.add(new Note("Báo cáo BTL", sdf.format(new Date()), "Viết tài liệu và demo ứng dụng"));
    }

    // Tạo Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lvNotes) {
            getMenuInflater().inflate(R.menu.my_context_menu, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // Xử lý Context Menu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedIndex = info.position;

        int id = item.getItemId();
        if (id == R.id.menu_edit) {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            intent.putExtra("note", lstNotes.get(selectedIndex));
            editNoteLauncher.launch(intent);
            return true;
        } else if (id == R.id.menu_refresh) {
            // 🔹 Đọc lại dữ liệu từ file myData.txt
            ArrayList<Note> refreshedNotes = loadFromFile();

            if (refreshedNotes != null && !refreshedNotes.isEmpty()) {
                lstNotes.clear(); // Xóa list cũ
                lstNotes.addAll(refreshedNotes); // Thêm dữ liệu mới đọc
                adapter.notifyDataSetChanged(); // Cập nhật ListView
                Toast.makeText(this, "Đã refresh dữ liệu từ file myData.txt!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File rỗng hoặc chưa có dữ liệu để refresh!", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

        return super.onContextItemSelected(item);
    }

    // Hộp thoại xác nhận khi đóng
    private void confirmCloseApp() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn lưu và thoát ứng dụng không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    saveToFile();
                    Toast.makeText(MainActivity.this, "Đã lưu dữ liệu và thoát ứng dụng", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Không", null)
                .show();
    }

    // Lưu danh sách vào file
    private void saveToFile() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            StringBuilder sb = new StringBuilder();
            for (Note n : lstNotes) {
                sb.append(n.getTitle()).append("|").append(n.getDate()).append("|").append(n.getContent()).append("\n");
            }
            fos.write(sb.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đọc danh sách từ file
    private ArrayList<Note> loadFromFile() {
        ArrayList<Note> notes = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    notes.add(new Note(parts[0], parts[1], parts[2]));
                }
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            // nếu chưa có file -> trả về rỗng
            return new ArrayList<>();
        }
        return notes;
    }
}