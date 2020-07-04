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
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginMasterM;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvSkip, tvLogin;
    EditText etUserName, etEmail, etPhone, etPassword, etConfirmPassword;
    Button btnRegister, btnLogin;
    PreManager preManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvSkip = (TextView) findViewById(R.id.tvSkip);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        etUserName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
        preManager = new PreManager(RegisterActivity.this);

    }

    @Override
    public void onClick(View v) {
        if (v == tvSkip) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else if (v == btnRegister) {
            if(CommonUtils.isNetworkConnected(RegisterActivity.this)){
                if (checkValidation()) {
                    registerApiCall();
                }
            }else{
                Toast.makeText(this, ""+getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }


        } else if (v == btnLogin) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    private void registerApiCall() {
        CommonUtils.show(RegisterActivity.this);
        AndroidNetworking.post(ApiUrl.registerUser)
                .addBodyParameter(ApiParams.email, etEmail.getText().toString().trim())
                .addBodyParameter(ApiParams.password, etPassword.getText().toString().trim())
                .addBodyParameter(ApiParams.userName, etUserName.getText().toString().trim())
                .addBodyParameter(ApiParams.mobile, AppConstant.PHONE_CODE + etPhone.getText().toString().trim())
                .addBodyParameter(ApiParams.token, "56451564654464654")
                .setTag(ApiUrl.registerUser)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.hide();
                        LoginMasterM loginMasterM = new GsonBuilder().create().fromJson(response.toString(), LoginMasterM.class);
                        if (loginMasterM.getSuccess() == 1) {
                            preManager.setLoginDetails(loginMasterM.getResult());
                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(RegisterActivity.this, "" + loginMasterM.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (etUserName.getText().toString().isEmpty()) {
            etUserName.setError(getString(R.string.please_enter_username));
            return false;
        } else if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.please_enter_email));
            return false;
        } else if (!CommonUtils.isValidEmailAddress(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.please_enter_valid_email));
            return false;
        } else if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError(getString(R.string.please_enter_phone));
            return false;
        } else if (etPhone.getText().toString().length() != 10) {
            etPhone.setError(getString(R.string.please_enter_valid_phone));
            return false;
        } else if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.please_enter_password));
            return false;
        } else if (etConfirmPassword.getText().toString().isEmpty()) {
            etConfirmPassword.setError(getString(R.string.please_enter_password));
            return false;
        } else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
            etConfirmPassword.setError(getString(R.string.password_not_matching));
            etPassword.setError(getString(R.string.password_not_matching));
            return false;
        } else {
            return true;
        }

    }
}
