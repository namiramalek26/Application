package com.hotapps.easyplant.model.serverPlants;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServerPlantMasterM implements Serializable {
    @SerializedName("pid")
    @Expose
    private String pid;
    @SerializedName("basic")
    @Expose
    private Basic basic;
    @SerializedName("display_pid")
    @Expose
    private String displayPid;
    @SerializedName("maintenance")
    @Expose
    private Maintenance maintenance;
    @SerializedName("parameter")
    @Expose
    private Parameter parameter;
    @SerializedName("image")
    @Expose
    private String image;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getDisplayPid() {
        return displayPid;
    }

    public void setDisplayPid(String displayPid) {
        this.displayPid = displayPid;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
