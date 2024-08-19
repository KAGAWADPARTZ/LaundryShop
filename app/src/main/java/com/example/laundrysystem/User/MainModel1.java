package com.example.laundrysystem.User;

public class MainModel1 {


    String shop_name, address;

    public MainModel1() {
    }

    public MainModel1(String shop_name, String address) {
        this.shop_name = shop_name;
        this.address = address;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setName(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
