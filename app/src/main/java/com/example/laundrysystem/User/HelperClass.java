package com.example.laundrysystem.User;

public class HelperClass {


    String name,email,password,contactNumber, laundryName,laundryPrice,laundryServices,laundryLocation, address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLaundryName(){return laundryName;}

    public void setLaundryName(String laundryName){this.laundryName =laundryName;}

    public String getlaundryPrice(){return laundryPrice;}

    public void setlaundryPrice(String laundryPrice){this.laundryPrice =laundryName;}

    public String getlaundryServices(){return laundryServices;}

    public void setlaundryServices(String laundryServices){this.laundryServices =laundryName;}

    public String getlaundryLocation(){return laundryLocation;}

    public void setlaundryLocation(String laundryLocation){this.laundryLocation =laundryName;}

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

    public String getAddress(){return  address;}

    public  void setAddress(String address){this.address = address;}

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public HelperClass(String name, String email, String password, String contactNumber, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNumber = contactNumber;
        this.address = address;
    }
    public HelperClass() {
    }
}
