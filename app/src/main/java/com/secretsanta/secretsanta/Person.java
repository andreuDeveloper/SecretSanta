package com.secretsanta.secretsanta;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
    private String birthday;
    private Bitmap image;
    private String likes;
    private boolean hasImage;
    private boolean havePersonAsociated;

    public Person() {
        id = UUID.randomUUID();
        havePersonAsociated = false;
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

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public void setBirthday(String b){
        this.birthday = b;
    }

    public String getBirthday(){
        return this.birthday;
    }


    public boolean havePersonAsociated(){
        return this.havePersonAsociated;
    }

    public void setHavePersonAsociated(boolean b){
        this.havePersonAsociated = b;
    }
}
