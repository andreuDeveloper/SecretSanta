package com.secretsanta.secretsanta;


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
    private ArrayList<String> likes;

    public Person() {
        id = UUID.randomUUID();
        name = id.toString()+"name";
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
