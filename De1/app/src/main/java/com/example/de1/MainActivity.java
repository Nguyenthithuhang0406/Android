package com.example.de1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Spinner spForum;
    CheckBox cbGame, cbWeb;
    EditText edtMessage;
    ListView lvPosts;

    ArrayList<Post> list;
    ArrayAdapter<Post> adapter;

    // Đếm số lần đăng ký (Câu 2)
    int registrationCount = 0;

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

        // Dữ liệu Spinner
        String[] forums = {"Sinh viên", "Học tập", "Giải trí", "Công nghệ"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, forums);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spForum.setAdapter(spinnerAdapter);

        // ListView
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        lvPosts.setAdapter(adapter);
    }

    public void mapping() {
        spForum = findViewById(R.id.spForum);
        cbGame = findViewById(R.id.cbGame);
        cbWeb = findViewById(R.id.cbWeb);
        edtMessage = findViewById(R.id.edtMessage);
        lvPosts = findViewById(R.id.lvPosts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAdd) {
            addPost();          // Câu 2
            return true;
        } else if (id == R.id.menuSave) {
            saveObjectToFile(MainActivity.this, list);   // Câu 3 (lưu bộ nhớ trong)
            return true;
        } else if (id == R.id.menuView) {
            viewResults();      // Câu 4 (mở Activity2)
            return true;
        } else if (id == R.id.menuExit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Câu 2: thêm 1 lần đăng ký, chỉ tăng nếu đủ thông tin
    private void addPost() {
        String forum = spForum.getSelectedItem().toString();
        String message = edtMessage.getText().toString().trim();

        String hobbies = "";
        if (cbGame.isChecked()) hobbies += "Chơi game, ";
        if (cbWeb.isChecked()) hobbies += "Lướt web, ";
        if (hobbies.endsWith(", ")) hobbies = hobbies.substring(0, hobbies.length() - 2);

        // Kiểm tra đủ thông tin (ở đây: message không được rỗng)
        if (message.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập lời nhắn!", Toast.LENGTH_SHORT).show();
            return;
        }

        Post p = new Post(forum, hobbies, message);
        list.add(p);
        adapter.notifyDataSetChanged();

        // Tăng bộ đếm và thông báo (Câu 2)
        registrationCount++;
        Toast.makeText(this, "Đã thêm bài đăng! (Số lần đăng ký: " + registrationCount + ")", Toast.LENGTH_SHORT).show();

        // reset input
        edtMessage.setText("");
        cbGame.setChecked(false);
        cbWeb.setChecked(false);
    }

    //Câu 3: Lưu file
    public boolean saveObjectToFile(Context context, List<Post> arrStds){
        FileOutputStream fileOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try{
            fileOutputStream=context.openFileOutput(MyConstant.OBJECT_FILENAME,Context.MODE_PRIVATE);
            objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(arrStds);
            objectOutputStream.close();
            fileOutputStream.close();
            Toast.makeText(this, "Lưu file thành công!", Toast.LENGTH_SHORT).show();
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    // Câu 4: Mở Activity2 và truyền dữ liệu
    private void viewResults() {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//        intent.putExtra("posts", list); // Post implements Serializable
        startActivity(intent);
    }
}