package com.example.bdranrestaurant.CartPackage;

public class OrderItems {
    String image;
    String name;
    String desc;
    String price;
    String total_price;
    String order_requested_number;

    public OrderItems(String image, String name, String desc, String price, String total_price,String order_requested_number) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.total_price = total_price;
        this.order_requested_number=order_requested_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getOrder_requested_number() {
        return order_requested_number;
    }

    public void setOrder_requested_number(String order_requested_number) {
        this.order_requested_number = order_requested_number;
    }
}
