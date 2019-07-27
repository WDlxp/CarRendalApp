package com.example.carrendalapp.entity;

/**
 * @author H
 */
public class CarOrder {
    private String name;
    private String carNumber;
    private String carBand;
    private String freeTime;
    private String pointTime;
    /**
     * 是租车客户电话号码，而非车主电话号码
     */
    private String tel;
    private int check;
    private int status;

    public void setName(String name) {
        this.name = name;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setCarBand(String carBand) {
        this.carBand = carBand;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    public void setPointTime(String pointTime) {
        this.pointTime = pointTime;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getCarBand() {
        return carBand;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public String getPointTime() {
        return pointTime;
    }

    public String getTel() {
        return tel;
    }

    public int getCheck() {
        return check;
    }

    public int getStatus() {
        return status;
    }

    public CarOrder(String name, String carNumber, String carBand, String freeTime, String pointTime, String tel, int check, int status) {
        this.name = name;
        this.carNumber = carNumber;
        this.carBand = carBand;
        this.freeTime = freeTime;
        this.pointTime = pointTime;
        this.tel = tel;
        this.check = check;
        this.status = status;
    }
}
