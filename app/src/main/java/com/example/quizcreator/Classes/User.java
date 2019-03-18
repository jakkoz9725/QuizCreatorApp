package com.example.quizcreator.Classes;

public class User {
    private String email;
    private String userName;
    private String userPassword;

    public User() {
    }

    public User(String email, String userName, String userPassword){
        this.email = email;
        this.userName = userName;
        this.userPassword = userPassword;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
