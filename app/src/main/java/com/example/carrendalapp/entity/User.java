package com.example.carrendalapp.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 这边实现序列化的接口用于传递对象
 *
 * @author WD
 */
public class User implements Parcelable {
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

    protected User(Parcel in) {
        imageName = in.readString();
        account = in.readString();
        password = in.readString();
        name = in.readString();
        gender = in.readInt();
        tel = in.readString();
        manager = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageName);
        parcel.writeString(account);
        parcel.writeString(password);
        parcel.writeString(name);
        parcel.writeInt(gender);
        parcel.writeString(tel);
        parcel.writeInt(manager);
    }
}
