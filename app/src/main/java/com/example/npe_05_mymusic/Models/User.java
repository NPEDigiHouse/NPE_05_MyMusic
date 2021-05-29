package com.example.npe_05_mymusic.Models;

public class User {
    public  String fullName, email;

    public User(){

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public  User(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }
}
