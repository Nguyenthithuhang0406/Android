package com.example.manageruser;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final ArrayList<String> data = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private TextView inputName, inputPhone;
    private RadioButton radioAdmin, radioUser, radioGuest;
    private Button btnAdd, btnDelete, btnExit;

    private boolean isUpdate = false;
    private int indexUpdate = -1;

    private void fakeData() {
        data.add("Nguyen Van A - 0123456789: Admin");
        data.add("Tran Thi B - 0987654321: User");
        data.add("Le Van C - 0112233445: Guest");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        fakeData();
        initViews();
        setupListView();
        setupButton();
    }

    private void initViews() {
        listView = findViewById(R.id.listView);
        inputName = findViewById(R.id.inputName);
        inputPhone = findViewById(R.id.inputPhone);
        radioAdmin = findViewById(R.id.radioAdmin);
        radioUser = findViewById(R.id.radioUser);
        radioGuest = findViewById(R.id.radioGuest);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnExit = findViewById(R.id.btnExit);
    }

    private void setupListView() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String user = data.get(position);

            // Tách dữ liệu
            String[] parts = user.split(" - ");
            String name = parts[0].trim();
            String[] parts2 = parts[1].split(":");
            String phone = parts2[0].trim();
            String role = parts2[1].trim();

            // Đổ dữ liệu lên form
            inputName.setText(name);
            inputPhone.setText(phone);
            setRole(role);

            //Cập nhật trạng thái
            isUpdate = true;
            indexUpdate = position;
            btnAdd.setText("Update");
        });
    }

    private void setupButton() {
        btnAdd.setOnClickListener(v -> {
            // Lấy dữ liệu từ form
            String name = inputName.getText().toString().trim();
            String phone = inputPhone.getText().toString().trim();
            String role = getSelectedRole();

            if (!validateInput(name, phone, role)) return;

            String userInfo = name + " - " + phone + ": " + role;

            if (isUpdate && indexUpdate >= 0 && indexUpdate < data.size()) {
                //sửa dữ liệu
                data.set(indexUpdate, userInfo);
                Toast.makeText(this, "Cập nhật người dùng thành công", Toast.LENGTH_SHORT).show();
                //cập nhật trạng thái
                isUpdate = false;
                indexUpdate = -1;
                btnAdd.setText("Add");
            } else {
                //thêm dữ liệu
                data.add(userInfo);
                Toast.makeText(this, "Thêm người dùng thành công", Toast.LENGTH_SHORT).show();
            }

            //Cập nhật giao diện
            adapter.notifyDataSetChanged();
            resetForm();
        });

        btnDelete.setOnClickListener(v -> {
            if (isUpdate && indexUpdate >= 0 && indexUpdate < data.size()) {
                new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa người dùng này không?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            data.remove(indexUpdate);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Xóa người dùng thành công", Toast.LENGTH_SHORT).show();

                            isUpdate = false;
                            indexUpdate = -1;
                            btnAdd.setText("Add");
                            resetForm();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                showToast("Vui lòng chọn người dùng để xóa");
            }
        });

        btnExit.setOnClickListener(v -> {
            // Hiển thị hộp thoại xác nhận
            new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("Xác nhận thoát")
                    .setMessage("Bạn có chắc chắn muốn thoát ứng dụng không?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity(); // đóng toàn bộ ứng dụng
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private boolean validateInput(String name, String phone, String role) {
        if (name.length() < 2) {
            showToast("Name cần có ít nhất 2 ký tự");
            return false;
        }
        if (!phone.matches("\\d+")) {
            showToast("Số điện thoại chỉ được nhập số");
            return false;
        }
        if (role.isEmpty()) {
            showToast("Vui lòng chọn vai trò");
            return false;
        }
        return true;
    }

    private void resetForm() {
        inputName.setText("");
        inputPhone.setText("");
        radioAdmin.setChecked(false);
        radioUser.setChecked(false);
        radioGuest.setChecked(false);
    }

    private String getSelectedRole() {
        if (radioAdmin.isChecked()) return "Admin";
        if (radioUser.isChecked()) return "User";
        if (radioGuest.isChecked()) return "Guest";
        return "";
    }

    private void setRole(String role) {
        radioAdmin.setChecked("Admin".equals(role));
        radioUser.setChecked("User".equals(role));
        radioGuest.setChecked("Guest".equals(role));
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
