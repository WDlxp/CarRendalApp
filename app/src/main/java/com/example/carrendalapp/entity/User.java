package com.example.carrendalapp.entity;

/**
 * @author WD
 */
public class User {
    private String imageName;
    private String account;
    private String password;
    private String name;
    private int gender;
    private String tel;
    private int manager;

    public User(String imageName, String account, String password, String name, int gender, String tel, int manager) {
        this.imageName = imageName;
        this.account = account;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.tel = tel;
        this.manager = manager;
    }

    public String getImageName() {
        return imageName;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getTel() {
        return tel;
    }

    public boolean isManager() {
        return manager == 0;
    }
}
