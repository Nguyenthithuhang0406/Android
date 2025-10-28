package com.example.managernote;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {
    private String title;
    private String content;
    private String date;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = new SimpleDateFormat("EEE, MMM d yyyy HH:mm:ss").format(new Date());
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return title + "\n" + content + "\n" + date + "\n--------------------------\n";
    }
}
