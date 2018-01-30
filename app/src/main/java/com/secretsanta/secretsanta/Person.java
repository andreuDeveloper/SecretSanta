package com.secretsanta.secretsanta;


import java.util.ArrayList;

/**
 * Created by andres on 30/01/2018.
 */

public class Person {
    private String name;
    private String email;
    private ArrayList<String> likes;

    public Person() {

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

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }
}
