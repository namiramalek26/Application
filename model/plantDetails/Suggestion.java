package com.hotapps.easyplant.model.plantDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Suggestion implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("plant")
    @Expose
    private Plant plant;
    @SerializedName("probability")
    @Expose
    private Double probability;
    @SerializedName("confidence")
    @Expose
    private Double confidence;
    @SerializedName("similar_images")
    @Expose
    private Object similarImages;
    @SerializedName("confirmed")
    @Expose
    private Boolean confirmed;
    private final static long serialVersionUID = 6864694359701688257L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public Object getSimilarImages() {
        return similarImages;
    }

    public void setSimilarImages(Object similarImages) {
        this.similarImages = similarImages;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }
}
