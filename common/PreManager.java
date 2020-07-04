package com.hotapps.easyplant.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.model.LoginDetailsM;

import static android.content.Context.MODE_PRIVATE;

public class PreManager {

    Context context;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public PreManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(AppConstant.prefernceName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLoginDetails(LoginDetailsM loginDetails) {
        String strLogin = new GsonBuilder().create().toJson(loginDetails);
        editor.putString(AppConstant.loginDetaila, strLogin);
        editor.commit();
        editor.apply();
    }

    public LoginDetailsM getLoginDetails() {
        String loginStr = sharedPreferences.getString(AppConstant.loginDetaila, "");
        LoginDetailsM loginDetailsM = new GsonBuilder().create().fromJson(loginStr, LoginDetailsM.class);
        return loginDetailsM;
    }
}
