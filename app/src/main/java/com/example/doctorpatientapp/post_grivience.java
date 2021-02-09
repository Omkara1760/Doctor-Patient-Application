package com.example.doctorpatientapp;

@SuppressWarnings("ALL")
public class post_grivience {
    private String description, image;

    public post_grivience(String description, String image) {
        this.description = description;
        this.image = image;
    }


    public post_grivience() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
