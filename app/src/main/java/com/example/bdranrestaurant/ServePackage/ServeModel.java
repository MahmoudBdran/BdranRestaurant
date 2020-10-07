package com.example.bdranrestaurant.ServePackage;

public class ServeModel {
    String orderName;
    String orderNumber;
    String orderTotalPrice;

    public ServeModel(String orderName, String orderNumber, String orderTotalPrice) {
        this.orderName = orderName;
        this.orderNumber = orderNumber;
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(String orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }
}
