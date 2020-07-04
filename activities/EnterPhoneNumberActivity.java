package com.hotapps.easyplant.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.LoginMasterM;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class EnterPhoneNumberActivity extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth mAuth;
    TextInputEditText editTextCountryCode, editTextPhone;
    AppCompatButton buttonContinue;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivBack, ivImage;
    PreManager preManager;
    LoginDetailsM loginDetailsM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone_number);
        preManager = new PreManager(EnterPhoneNumberActivity.this);
        editTextCountryCode = findViewById(R.id.editTextCountryCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonContinue = findViewById(R.id.buttonContinue);
        progressBar = (ProgressBar) findViewById(R.id.progresBar);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivImage = (ImageView) findViewById(R.id.ivFlower);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        mAuth = FirebaseAuth.getInstance();
        tvTitle.setText(R.string.text_phone);


        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = editTextCountryCode.getText().toString().trim();
                String number = editTextPhone.getText().toString().trim();
                if (number.isEmpty() || number.length() < 10) {
                    editTextPhone.setError("Valid number is required");
                    editTextPhone.requestFocus();
                    return;
                }
                String phoneNumber = code + number;
                getUserDetailsFromMobile();
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.VISIBLE);
            verificationId = s;
            Intent intent = new Intent(EnterPhoneNumberActivity.this, VerifyPhoneNumberActivity.class);
            intent.putExtra(AppConstant.verificationId, verificationId);
            intent.putExtra(AppConstant.LOGIN_OBJECT, loginDetailsM);
            startActivity(intent);
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            progressBar.setVisibility(View.GONE);


        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Log.e("firebaseException", e.getMessage());
            buttonContinue.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    };

    private void getUserDetailsFromMobile() {
        progressBar.setVisibility(View.VISIBLE);
        buttonContinue.setVisibility(View.GONE);
        AndroidNetworking.post(ApiUrl.getUserDetailsFromMobile)
                .addBodyParameter(ApiParams.email, AppConstant.PHONE_CODE + editTextPhone.getText().toString().trim())
                .setTag(ApiUrl.getUserDetailsFromMobile)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoginMasterM loginMasterM = new GsonBuilder().create().fromJson(response.toString(), LoginMasterM.class);
                        if (loginMasterM.getSuccess() == 1) {
                            //preManager.setLoginDetails(loginMasterM.getResult());
                            loginDetailsM = loginMasterM.getResult();
                            sendVerificationCode(editTextCountryCode.getText().toString().trim() + editTextPhone.getText().toString().trim());
                        } else {
                            progressBar.setVisibility(View.GONE);
                            buttonContinue.setVisibility(View.VISIBLE);
                            Toast.makeText(EnterPhoneNumberActivity.this, "No user exists with this mobile", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("response", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        buttonContinue.setVisibility(View.VISIBLE);
                        Log.e("Error", anError.getMessage());
                    }
                });
    }
}
