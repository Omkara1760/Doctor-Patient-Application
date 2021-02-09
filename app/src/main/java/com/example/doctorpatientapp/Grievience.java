package com.example.doctorpatientapp;

public class Grievience {
    private String description,image;

    public Grievience(String description, String image){
        this.description = description;
        this.image = image;
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
