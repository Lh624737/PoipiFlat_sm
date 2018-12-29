package com.pospi.dto;

/**
 * Created by acer on 2017/6/16.
 */

public class OrderMenu {
    private String sid;
    private String name;
    private double price;
    private double number;
    private double totalPrice;
    private double discount;
    private String orderSid;

    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalPrice() {
        return price*number;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
