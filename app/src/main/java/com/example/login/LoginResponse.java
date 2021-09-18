package com.example.login;

public class LoginResponse {


    private String uid;
    private int role;
    private token token;


    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setToken(com.example.login.token token) {
        this.token = token;
    }


    public String getUid() {
        return uid;
    }

    public int getRole() {
        return role;
    }

    public com.example.login.token getToken() {
        return token;
    }

      /* public LoginResponse(String uid, int role) {
        this.uid = uid;
        this.role = role;
    }*/





}
