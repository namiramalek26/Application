package com.hotapps.easyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.LoginMasterM;

import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivBack;

    Button btnSave;
    private EditText  etPassword, etConfirmPassword;
    LoginDetailsM loginDetailsM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar =(Toolbar)findViewById(R.id.appBar);
        ivBack =(ImageView)findViewById(R.id.ivBack);
        tvTitle =(TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.chnge_password));
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnSave =(Button)findViewById(R.id.btnSave);
        Intent i = getIntent();

        if(i.hasExtra(AppConstant.LOGIN_OBJECT)){
            loginDetailsM = (LoginDetailsM) i.getExtras().get(AppConstant.LOGIN_OBJECT);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            upadetProfile();
            }
        });
    }

    private void upadetProfile() {
        CommonUtils.show(ChangePasswordActivity.this);

        ANRequest.PostRequestBuilder anPostRequestBuilder = new ANRequest.PostRequestBuilder(ApiUrl.updateProfile);
        if (!etPassword.getText().toString().isEmpty()) {
            anPostRequestBuilder.addBodyParameter(ApiParams.password, etConfirmPassword.getText().toString());
        }
        anPostRequestBuilder.addBodyParameter(ApiParams.userName, loginDetailsM.getUserName())
                .addBodyParameter(ApiParams.mobile, loginDetailsM.getUserMobile())
                .addBodyParameter(ApiParams.userId, loginDetailsM.getId())
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
                            Intent i = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, ""+loginMasterM.getMessage(), Toast.LENGTH_SHORT).show();
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
}
