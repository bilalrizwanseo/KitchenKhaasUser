package com.example.technohem.kitchenkhaasuser.Model;

// Recycler View code

public class Menus {

    private String pid, pname, price, image, date, time;

    public Menus() {
    }

    public Menus(String pid, String pname, String price, String image, String date, String time) {
        this.pid = pid;
        this.pname = pname;
        this.price = price;
        this.image = image;
        this.date = date;
        this.time = time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
