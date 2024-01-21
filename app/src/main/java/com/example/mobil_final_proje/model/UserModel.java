package com.example.mobil_final_proje.model;
public class UserModel {
    String name,lastname,email,UİD;
    public  UserModel(){}
    public UserModel(String name,String lastname,String email,String UİD){
        this.name=name;
        this.lastname=lastname;
        this.email=email;
        this.UİD=UİD;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUİD() {
        return UİD;
    }
    public void setUİD(String UİD) {
        this.UİD = UİD;
    }
}
