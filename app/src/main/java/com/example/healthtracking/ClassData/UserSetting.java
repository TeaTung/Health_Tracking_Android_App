package com.example.healthtracking.ClassData;

public class UserSetting {
    public boolean wasLogin;
    public String UID = "";
    public  boolean wasInformation;

    public UserSetting(){

    }

    public boolean isLogin() {
        return wasLogin;
    }

    public void setLogin(boolean login) {
        wasLogin = login;
    }

    public boolean isInfor() {
        return wasInformation;
    }

    public void setInfor(boolean infor) {
        wasInformation = infor;
    }
}
