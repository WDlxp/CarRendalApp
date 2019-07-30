package com.example.carrendalapp.entity;

/**
 * @author WD
 */
public class Order {
    private String account;
    private String carNumber;
    private String startDate;
    private String startTime;
    private String finishDate;
    private String finishTime;
    private int state;

    public Order(String account, String carNumber, String startDate, String startTime, String finishDate, String finishTime, int state) {
        this.account = account;
        this.carNumber = carNumber;
        this.startDate = startDate;
        this.startTime = startTime;
        this.finishDate = finishDate;
        this.finishTime = finishTime;
        this.state = state;
    }

    public String getAccount() {
        return account;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
