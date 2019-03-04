package com.example.firebaseexampleproject;

public class Note {

     String title;
     String description;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}