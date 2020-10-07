package com.example.bdranrestaurant.AdminPackage;

import java.util.List;

public class AdminOrderItem {
    String username;
    String phonenumber;
    String uid;
//    String userId;
//    int userorders;
//    List<AdminOrderItemSubItems>userOrdersList;

    public AdminOrderItem(String username, String phonenumber,String uid) {
        this.username = username;
        this.phonenumber = phonenumber;
//        this.userId = userId;
//        this.userorders=userorders;
//        this.userOrdersList=userOrdersList;
        this.uid=uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
