package com.example.technohem.kitchenkhaasuser.Model;

public class MyOrders {

    private String name, phone, date, time, state, address, description, totalAmount, event;

    public MyOrders() {
    }

    public MyOrders(String name, String phone, String date, String time, String state, String address, String description, String totalAmount, String event) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.state = state;
        this.address = address;
        this.description = description;
        this.totalAmount = totalAmount;
        this.event = event;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
