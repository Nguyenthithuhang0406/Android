package com.example.de1;

import java.io.Serializable;

public class Post implements Serializable {
    private String forum;
    private String hobbies;
    private String message;

    public Post(String forum, String hobbies, String message) {
        this.forum = forum;
        this.hobbies = hobbies;
        this.message = message;
    }

    public String getForum() { return forum; }
    public String getHobbies() { return hobbies; }
    public String getMessage() { return message; }

    @Override
    public String toString() {
        return forum + " - " + hobbies + " - " + message;
    }
}
