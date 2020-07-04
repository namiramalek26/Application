package com.hotapps.easyplant.model.plantList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlantListMasterM implements Serializable {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("searchList")
    @Expose
    private List<PlantDetailsM> searchList = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<PlantDetailsM> getSearchList() {
        return searchList;
    }

    public void setSearchList(List<PlantDetailsM> searchList) {
        this.searchList = searchList;
    }
}
