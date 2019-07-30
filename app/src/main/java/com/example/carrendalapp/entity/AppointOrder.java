package com.example.carrendalapp.entity;

/**
 * 返回预约订单的实体类
 * @author WD
 */
public class AppointOrder {
    private String name;
    private String tel;
    private String carBrand;
    private String image;
    private String freeTime;
    private Order order;

    public AppointOrder(String name, String tel, String carBrand, String image,String freeTime, Order order) {
        this.name = name;
        this.tel = tel;
        this.carBrand = carBrand;
        this.image = image;
        this.freeTime=freeTime;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getImage() {
        return image;
    }

    public String getFreeTime() {
        return freeTime;
    }

    public Order getOrder() {
        return order;
    }
}
