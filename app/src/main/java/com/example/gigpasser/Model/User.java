package com.example.gigpasser.Model;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gigpasser.R;
public class User {

    String name, lefteye, righteye, id, email, idNumber, lensOrFrame, phoneNumber, search, type;

    public User() {
    }

    public User(String name, String lefteye, String righteye, String id, String email, String idNumber, String lensOrFrame, String phoneNumber, String search, String type) {
        this.name = name;
        this.lefteye = lefteye;
        this.righteye = righteye;
        this.id = id;
        this.email = email;
        this.idNumber = idNumber;
        this.lensOrFrame = lensOrFrame;
        this.phoneNumber = phoneNumber;
        this.search = search;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLefteye() {
        return lefteye;
    }

    public void setLefteye(String lefteye) {
        this.lefteye = lefteye;
    }

    public String getRighteye() {
        return righteye;
    }

    public void setRighteye(String righteye) {
        this.righteye = righteye;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getLensOrFrame() {
        return lensOrFrame;
    }

    public void setLensOrFrame(String lensOrFrame) {
        this.lensOrFrame = lensOrFrame;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
