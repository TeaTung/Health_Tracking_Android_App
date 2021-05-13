package com.example.healthtracking.ClassData;

public class UserSetting {
    public boolean wasLogin;
    public String email = "";

    public UserSetting(){

    }

    public boolean isLogin() {
        return wasLogin;
    }

    public void setLogin(boolean login) {
        wasLogin = login;
    }
}
