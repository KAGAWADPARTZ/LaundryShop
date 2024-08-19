package com.example.laundrysystem.LaundryOwners;

public class LaundryMainModel {
    String address, shop_name, name,contactNumber, location, status;

    public LaundryMainModel() {
    }



    public LaundryMainModel(String address, String shop_name, String name, String contactNumber, String location, String status) {
        this.address = address;
        this.shop_name = shop_name;
        this.name = name;
        this.contactNumber = contactNumber;
        this.location = location;
        this.status = status;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}


}