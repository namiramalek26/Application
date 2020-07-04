package com.hotapps.easyplant.model.plantDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlantMasterM implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("created")
    @Expose
    private Double created;
    @SerializedName("sent")
    @Expose
    private Integer sent;
    @SerializedName("classified")
    @Expose
    private Double classified;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("suggestions")
    @Expose
    private List<Suggestion> suggestions = null;

    @SerializedName("secret")
    @Expose
    private String secret;

    @SerializedName("countable")
    @Expose
    private Boolean countable;
    @SerializedName("source")
    @Expose
    private String source;
    private final static long serialVersionUID = 3742617358098303404L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCreated() {
        return created;
    }

    public void setCreated(Double created) {
        this.created = created;
    }

    public Integer getSent() {
        return sent;
    }

    public void setSent(Integer sent) {
        this.sent = sent;
    }

    public Double getClassified() {
        return classified;
    }

    public void setClassified(Double classified) {
        this.classified = classified;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }


    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Boolean getCountable() {
        return countable;
    }

    public void setCountable(Boolean countable) {
        this.countable = countable;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}








