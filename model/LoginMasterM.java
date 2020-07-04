package com.hotapps.easyplant.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginMasterM {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("result")
    @Expose
    private LoginDetailsM result;

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

    public LoginDetailsM getResult() {
        return result;
    }

    public void setResult(LoginDetailsM result) {
        this.result = result;
    }
}
