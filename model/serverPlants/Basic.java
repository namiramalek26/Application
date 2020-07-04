package com.hotapps.easyplant.model.serverPlants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Basic implements Serializable {

    @SerializedName("floral_language")
    @Expose
    private String floralLanguage;
    @SerializedName("origin")
    @Expose
    private String origin;
    @SerializedName("production")
    @Expose
    private String production;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("blooming")
    @Expose
    private String blooming;
    @SerializedName("color")
    @Expose
    private String color;

    public String getFloralLanguage() {
        return floralLanguage;
    }

    public void setFloralLanguage(String floralLanguage) {
        this.floralLanguage = "floral_language :"+ floralLanguage;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = "Origin :" + origin;
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = "Production : "+production;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = "Category : "+category;
    }

    public String getBlooming() {
        return blooming;
    }

    public void setBlooming(String blooming) {
        this.blooming = "Blooming : "+blooming;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = "Color : "+color;
    }
}
