package com.example.laundrysystem.Admin;

public class HelperClass {


    String username,email,password,contact_number,shop_name,location;



    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShop_name() {return shop_name;}

    public String getContact_number() {return contact_number;}

    public HelperClass(String username, String email, String password, String shop_name,String location,String contactNumber)
    {
        this.username = username;
        this.email = email;
        this.password = password;
        this.shop_name = shop_name;
        this.contact_number = contactNumber;
        this.location = location;
    }
    public HelperClass() {
    }
}
