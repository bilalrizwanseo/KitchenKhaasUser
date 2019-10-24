package com.example.technohem.kitchenkhaasuser.Model;

public class Cart {

    private String pname, price, guests, pid, image;

    public Cart() {
    }

    public Cart(String pname, String price, String guests, String pid, String image) {
        this.pname = pname;
        this.price = price;
        this.guests = guests;
        this.pid = pid;
        this.image = image;
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

    public String getGuests() {
        return guests;
    }

    public void setGuests(String guests) {
        this.guests = guests;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
