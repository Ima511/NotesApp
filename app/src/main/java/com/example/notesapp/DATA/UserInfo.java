package com.example.notesapp.DATA;

import java.io.Serializable;

public class UserInfo implements Serializable {

    String userName = "", userEmail = "", userPassword = "", mobileNumber = "";

    public UserInfo(){

    }

    public UserInfo(String name, String email , String mobile){
        userName = name ;
        userEmail =  email;
        mobileNumber = mobile;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
