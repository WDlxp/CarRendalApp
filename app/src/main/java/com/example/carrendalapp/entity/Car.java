package com.example.carrendalapp.entity;

/**
 * 租车信息
 * 租车的实体类
 *
 * @author WD
 */
public class Car {
    private String account;
    private String carNumber;
    private String carBand;
    private String image;
    private String freeTime;
    private int state;
    private int check;

    public Car(String account, String carNumber, String carBand, String image, String freeTime, int state, int check) {
        this.account = account;
        this.carNumber = carNumber;
        this.carBand = carBand;
        this.image = image;
        this.freeTime = freeTime;
        this.state = state;
        this.check = check;
    }

    public String getAccount() {
        return account;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarBand() {
        return carBand;
    }

    public String getImage() {
        return image;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public int getState() {
        return state;
    }

    public int getCheck() {
        return check;
    }
}
