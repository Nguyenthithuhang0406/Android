package com.example.kiemtratx2_de04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {
    EditText edtTitle, edtContent;
    Button btnSave;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        btnSave = findViewById(R.id.btnSave);

        note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            edtTitle.setText(note.getTitle());
            edtContent.setText(note.getContent());
        }

        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            String content = edtContent.getText().toString();
            String date = new SimpleDateFormat("EEE, MMM dd yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

            Note updated = new Note(title, date, content);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("updatedNote", updated);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}