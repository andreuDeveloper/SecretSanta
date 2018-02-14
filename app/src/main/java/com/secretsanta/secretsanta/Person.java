package com.secretsanta.secretsanta;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by andres on 30/01/2018.
 */

public class Person {
    private UUID id;
    private String name;
    private String email;
    private int age;
    private Bitmap image;
    private ArrayList<String> likes;
    private boolean hasImage;

    public Person() {
        id = UUID.randomUUID();
        name = id.toString()+"name";
        image = null;
        hasImage = false;
    }

    public boolean hasImage(){
        return hasImage;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
        hasImage = true;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }
}
