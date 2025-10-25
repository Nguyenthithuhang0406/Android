package com.example.managerstudent;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtName, edtBirthday;
    private Button btnSelectdate, btnNew, btnUpdate, btnRemove, btnClose;
    private RadioButton rdMale, rdfemale;
    private ListView lvStudents;
    private List<Student> arrStudents;
    private ArrayAdapter arrayAdapter;
    private int pos;

    private Toolbar myToolBar;
    private ActivityResultLauncher launcher;
    private LinearLayout linearLayout;

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

        setSupportActionBar(myToolBar);
        if (getSupportActionBar().getTitle() != null) {
            getSupportActionBar().setTitle("Demo menu &Intent");
        }
        //Dang ky context menu cho ListView
        registerForContextMenu(lvStudents);

        // Dang ky nhan ket qua tra ve
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                int selectedColor = intent.getIntExtra("selectedColor", 0);
                linearLayout.setBackgroundColor(selectedColor);
            }
        });

        arrStudents = new ArrayList<Student>();
        arrStudents.add(new Student("Le la", "15/02/2003", true));
        arrStudents.add(new Student("Le la2", "15/02/2003", false));
        arrStudents.add(new Student("Le la3", "15/02/2003", true));
        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, arrStudents);
        lvStudents.setAdapter(arrayAdapter);
        selectedStudent(0);

       lvStudents.setOnItemClickListener((parent, view, position, id) -> {
              pos = position;
              selectedStudent(pos);
       });

        btnSelectdate.setOnClickListener(v -> {
            selectDate();
        });

        btnRemove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Chu y");
            builder.setMessage("Ban co muon xoa khong?");
            builder.setPositiveButton("Xoa", (dialog, which) -> {
                arrStudents.remove(pos);
                arrayAdapter.notifyDataSetChanged();
                //xoa noi dung ban ghi bi xoa tren cac view
                selectedStudent(-1);
                if (arrStudents.isEmpty()) {
                    btnRemove.setEnabled(false);
                }
            });
            builder.setNegativeButton("Huy", null);
            builder.show();
        });

        btnNew.setOnClickListener(v -> {
            if (btnNew.getText().toString().equals("New")) {
                btnNew.setText("Save");
                btnRemove.setEnabled(false);
                btnUpdate.setEnabled(false);
                selectedStudent(-1);
            } else {
                Student student = getStudent();
                arrStudents.add(student);
                arrayAdapter.notifyDataSetChanged();
                btnNew.setText("New");
                btnRemove.setEnabled(true);
                btnUpdate.setEnabled(true);
                Toast.makeText(MainActivity.this, "Da them thanh cong", Toast.LENGTH_SHORT).show();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("chu y");
            builder.setMessage("Co chac chan muon cap nhat khong?");
            builder.setPositiveButton("Cap nhat", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    arrStudents.set(pos, getStudent());
                    arrayAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("Huy", null);
            builder.show();
        });

        btnClose.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("chu y");
            builder.setMessage("Co chac chan muon dong ung dung khong?");
            builder.setPositiveButton("Dong", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("Huy", null);
            builder.show();
        });
    }

    private Student getStudent() {
        Student student = new Student();
        student.setName(edtName.getText().toString());
        student.setBirthday(edtBirthday.getText().toString());
        if (rdMale.isChecked()) {
            student.setSex(true);
        } else {
            student.setSex(false);
        }
        return student;
    }

    private void selectDate() {
        //lay ngay hien tai
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //Tao DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                String date = d + "/" + (m + 1) + "/" + y;
                edtBirthday.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();

    }

    private void selectedStudent(int pos) {
        if (pos >= 0) {
            Student student = arrStudents.get(pos);
            edtName.setText(student.getName());
            edtBirthday.setText(student.getBirthday());
            if (student.isSex()) {
                rdMale.setChecked(true);
            } else {
                rdfemale.setChecked(true);
            }
        } else {
            edtName.setText("");
            edtBirthday.setText("");
            rdMale.setChecked(false);
            rdfemale.setChecked(false);
        }
    }


    private void mapping() {
        edtName = findViewById(R.id.edt_name);
        edtBirthday = findViewById(R.id.edt_birthday);
        rdMale = findViewById(R.id.rd_male);
        rdfemale = findViewById(R.id.rd_female);
        lvStudents = findViewById(R.id.lv_students);
        btnSelectdate = findViewById(R.id.btn_select_date);
        btnNew = findViewById(R.id.btn_new);
        btnUpdate = findViewById(R.id.btn_update);
        btnRemove = findViewById(R.id.btn_remove);
        btnClose = findViewById(R.id.btn_close);
        myToolBar = findViewById(R.id.my_toolbar);
        linearLayout = findViewById(R.id.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mni_search) {
            //  Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("msg", "Xin chao");
            startActivity(intent);
        } else {
            if (id == R.id.mni_settings) {
                Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,SettingActivity.class);
                intent.putExtra("msgRequest","Chon mau nen");
                launcher.launch(intent);
            } else {
                if (id == R.id.mni_share) {
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.my_context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mni_send) {
            Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            Student studentObj = arrStudents.get(pos);
            intent.putExtra("studentObj", studentObj);
            startActivity(intent);
        } else {
            if (id == R.id.mni_zalo) {
                Toast.makeText(this, "Zalo", Toast.LENGTH_SHORT).show();
            } else {
                if (id == R.id.mni_fb) {
                    Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
                }

            }
        }
        return super.onContextItemSelected(item);
    }
}