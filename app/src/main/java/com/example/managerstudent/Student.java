package com.example.managerstudent;

import java.io.Serializable;

public class Student  implements Serializable {
    private String name;
    private String birthday;
    private boolean sex;

    @Override
    public String toString() {
        String gt = "";
        if (this.sex) {
            gt = "Nam";
        } else {
            gt = "Nu";
        }
        return "Student{" +
                "Student: '" + name + '\'' +
                ", '" + birthday + '\'' +
                ", " + gt +
                '}';
    }

    public Student() {
    }

    public Student(String name, String birthday, boolean sex) {
        this.name = name;
        this.birthday = birthday;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
