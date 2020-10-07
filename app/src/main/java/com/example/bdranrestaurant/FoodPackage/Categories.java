package com.example.bdranrestaurant.FoodPackage;

public class Categories {
    String image ;
    String name;
    String desc;
    String price;


    public Categories(String image, String name, String desc, String price) {
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.price = price;
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
}
