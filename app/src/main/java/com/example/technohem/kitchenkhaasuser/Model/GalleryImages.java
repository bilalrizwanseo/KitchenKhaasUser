package com.example.technohem.kitchenkhaasuser.Model;

public class GalleryImages {

    private String image_id, g_image, date, time;

    public GalleryImages() {
    }

    public GalleryImages(String image_id, String g_image, String date, String time) {
        this.image_id = image_id;
        this.g_image = g_image;
        this.date = date;
        this.time = time;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getG_image() {
        return g_image;
    }

    public void setG_image(String g_image) {
        this.g_image = g_image;
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
