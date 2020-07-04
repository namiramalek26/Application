package com.hotapps.easyplant.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginMasterM;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvCreateNew, tvSkip,tvForgotPassword;
    EditText etEmail, etPassword;
    Button btnLogin;
    PreManager preManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvCreateNew = (TextView) findViewById(R.id.tvSignUp);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        tvCreateNew.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
        preManager = new PreManager(LoginActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvCreateNew) {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);

        } else if (v == btnLogin) {
            if(CommonUtils.isNetworkConnected(LoginActivity.this)){
                if (checkValidation()) {
                    LoginApiCall(); }
            }else{
                Toast.makeText(this, ""+getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        } else if (v == tvSkip) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }else if (v == tvForgotPassword){
            Intent i = new Intent(LoginActivity.this, EnterPhoneNumberActivity.class);
            startActivity(i);
        }

    }

    private void LoginApiCall() {
        CommonUtils.show(LoginActivity.this);
        AndroidNetworking.post(ApiUrl.login)
                .addBodyParameter(ApiParams.email, etEmail.getText().toString().trim())
                .addBodyParameter(ApiParams.password, etPassword.getText().toString().trim())
                .addBodyParameter(ApiParams.token, "56451564654464654")
                .setTag(ApiUrl.login)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.hide();
                        LoginMasterM loginMasterM = new GsonBuilder().create().fromJson(response.toString(), LoginMasterM.class);
                        if (loginMasterM.getSuccess() == 1) {
                            preManager.setLoginDetails(loginMasterM.getResult());
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "" + loginMasterM.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("response", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        CommonUtils.hide();
                        Log.e("Error", anError.getMessage());


                    }
                });
    }

    private boolean checkValidation() {
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.please_enter_email));
            return false;
        } else if (!CommonUtils.isValidEmailAddress(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.please_enter_valid_email));
            return false;

        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.please_enter_password));
            return false;

        } else {
            return true;
        }

    }
}
