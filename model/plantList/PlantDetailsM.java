package com.hotapps.easyplant.model.plantList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlantDetailsM implements Serializable {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("origin")
    @Expose
    private String origin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
