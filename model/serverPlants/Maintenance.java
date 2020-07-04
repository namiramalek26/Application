package com.hotapps.easyplant.model.serverPlants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Maintenance  implements Serializable {

    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("soil")
    @Expose
    private String soil;
    @SerializedName("sunlight")
    @Expose
    private String sunlight;
    @SerializedName("watering")
    @Expose
    private String watering;
    @SerializedName("fertilization")
    @Expose
    private String fertilization;
    @SerializedName("pruning")
    @Expose
    private String pruning;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getWatering() {
        return watering;
    }

    public void setWatering(String watering) {
        this.watering = watering;
    }

    public String getFertilization() {
        return fertilization;
    }

    public void setFertilization(String fertilization) {
        this.fertilization = fertilization;
    }

    public String getPruning() {
        return pruning;
    }

    public void setPruning(String pruning) {
        this.pruning = pruning;
    }


}

