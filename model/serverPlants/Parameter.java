package com.hotapps.easyplant.model.serverPlants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Parameter implements Serializable {
    @SerializedName("max_light_mmol")
    @Expose
    private Integer maxLightMmol;
    @SerializedName("min_light_mmol")
    @Expose
    private Integer minLightMmol;
    @SerializedName("max_light_lux")
    @Expose
    private Integer maxLightLux;
    @SerializedName("min_light_lux")
    @Expose
    private Integer minLightLux;
    @SerializedName("max_temp")
    @Expose
    private Integer maxTemp;
    @SerializedName("min_temp")
    @Expose
    private Integer minTemp;
    @SerializedName("max_env_humid")
    @Expose
    private Integer maxEnvHumid;
    @SerializedName("min_env_humid")
    @Expose
    private Integer minEnvHumid;
    @SerializedName("max_soil_moist")
    @Expose
    private Integer maxSoilMoist;
    @SerializedName("min_soil_moist")
    @Expose
    private Integer minSoilMoist;
    @SerializedName("max_soil_ec")
    @Expose
    private Integer maxSoilEc;
    @SerializedName("min_soil_ec")
    @Expose
    private Integer minSoilEc;

    public Integer getMaxLightMmol() {
        return maxLightMmol;
    }

    public void setMaxLightMmol(Integer maxLightMmol) {
        this.maxLightMmol = maxLightMmol;
    }

    public Integer getMinLightMmol() {
        return minLightMmol;
    }

    public void setMinLightMmol(Integer minLightMmol) {
        this.minLightMmol = minLightMmol;
    }

    public Integer getMaxLightLux() {
        return maxLightLux;
    }

    public void setMaxLightLux(Integer maxLightLux) {
        this.maxLightLux = maxLightLux;
    }

    public Integer getMinLightLux() {
        return minLightLux;
    }

    public void setMinLightLux(Integer minLightLux) {
        this.minLightLux = minLightLux;
    }

    public Integer getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Integer maxTemp) {
        this.maxTemp = maxTemp;
    }

    public Integer getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Integer minTemp) {
        this.minTemp = minTemp;
    }

    public Integer getMaxEnvHumid() {
        return maxEnvHumid;
    }

    public void setMaxEnvHumid(Integer maxEnvHumid) {
        this.maxEnvHumid = maxEnvHumid;
    }

    public Integer getMinEnvHumid() {
        return minEnvHumid;
    }

    public void setMinEnvHumid(Integer minEnvHumid) {
        this.minEnvHumid = minEnvHumid;
    }

    public Integer getMaxSoilMoist() {
        return maxSoilMoist;
    }

    public void setMaxSoilMoist(Integer maxSoilMoist) {
        this.maxSoilMoist = maxSoilMoist;
    }

    public Integer getMinSoilMoist() {
        return minSoilMoist;
    }

    public void setMinSoilMoist(Integer minSoilMoist) {
        this.minSoilMoist = minSoilMoist;
    }

    public Integer getMaxSoilEc() {
        return maxSoilEc;
    }

    public void setMaxSoilEc(Integer maxSoilEc) {
        this.maxSoilEc = maxSoilEc;
    }

    public Integer getMinSoilEc() {
        return minSoilEc;
    }

    public void setMinSoilEc(Integer minSoilEc) {
        this.minSoilEc = minSoilEc;
    }
}
