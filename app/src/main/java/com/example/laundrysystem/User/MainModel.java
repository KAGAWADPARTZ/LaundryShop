package com.example.laundrysystem.User;

public class MainModel {

    String shop_name,location,contact_number,services,name,address;

    public MainModel() {
    }



    public MainModel(String shop_name, String location, String contact_number,String services,String name,String address) {
        this.shop_name = shop_name;
        this.location = location;
        this.contact_number = contact_number;
        this.services = services;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

}
