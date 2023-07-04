package com.example.firstpage.model;

import java.io.Serializable;

public class ProductModel implements Serializable {

    private String key, name, image, price, description;

    public ProductModel() {
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getdescription() {return description;}

    public void setdescription(String desc) {this.description = desc;}
}
