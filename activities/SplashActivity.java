package com.hotapps.easyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;

public class SplashActivity extends AppCompatActivity {

    PreManager preManager;
    LoginDetailsM loginDetailsM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preManager = new PreManager(SplashActivity.this);
        loginDetailsM = preManager.getLoginDetails();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loginDetailsM != null){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);
    }
}
