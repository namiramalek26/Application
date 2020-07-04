package com.hotapps.easyplant;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

public class PlantApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(this);
    }
}
