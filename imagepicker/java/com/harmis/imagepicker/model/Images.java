package com.harmis.imagepicker.model;

import java.io.Serializable;

/**
 * Created by Paras Andani
 */
public class Images implements Serializable {
    String imageUrl;
    boolean isChecked;
    boolean isPdf;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isPdf() {
        return isPdf;
    }

    public void setPdf(boolean pdf) {
        isPdf = pdf;
    }
}
