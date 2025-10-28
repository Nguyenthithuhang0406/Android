package com.example.manageruser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvUsers;
    ArrayList<User> userList;
    ArrayAdapter<User> adapter;

    ActivityResultLauncher<Intent> addUserLauncher;

    private static final String FILE_NAME = "users.txt";

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

        lvUsers = findViewById(R.id.lvUsers);
        userList = readFromFile();

        // Nếu file trống thì thêm sẵn 2 dòng
        if (userList.isEmpty()) {
            userList.add(new User("Nguyễn Văn A", "0901234567"));
            userList.add(new User("Trần Thị B", "0987654321"));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);
        registerForContextMenu(lvUsers);

        // ActivityResultLauncher để nhận dữ liệu từ AddUserActivity
        addUserLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            User newUser = (User) data.getSerializableExtra("newUser");
                            if (newUser != null) {
                                userList.add(newUser);
                                adapter.notifyDataSetChanged();
                                saveToFile();
                            }
                        }
                    }
                }
        );
    }

    // Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnuAdd) {
            openAddUserActivity();
            return true;
        } else if (id == R.id.mnuExit) {
            confirmExit();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void openAddUserActivity() {
        Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
        addUserLauncher.launch(intent);
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn thoát không?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", null)
                .show();
    }

    // Ghi danh sách vào file
    private void saveToFile() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userList);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đọc danh sách từ file
    private ArrayList<User> readFromFile() {
        ArrayList<User> list = new ArrayList<>();
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<User>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            // lần đầu chưa có file
        }
        return list;
    }
}