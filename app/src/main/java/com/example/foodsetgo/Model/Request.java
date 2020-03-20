package com.example.foodsetgo.Model;

import java.util.List;

public class Request {
    private String phone, name, address, status, date_time, total;
    private List<Order> foods;

    public Request(String phone, String name, String address, String status, String date_time, String total, List<Order> foods){
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = status; //0:Placed 1:Accepted 2:On the way 3:Delivered 4:Rejected
        this.date_time = date_time;
        this.total = total;
        this.foods = foods;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getTotal() {
        return total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
