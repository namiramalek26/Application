package com.hotapps.easyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.adapter.ServerPlantDetailsAdapter;
import com.hotapps.easyplant.adapter.SuggetionAdapter;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.model.plantDetails.PlantMasterM;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;
import com.hotapps.easyplant.model.serverPlants.ServerPlantMasterM;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlantDetailsActivity extends AppCompatActivity {

    RecyclerView recFeed;
    RecyclerView recMaintainance;
    PlantMasterM plantMasterM;
    Toolbar toolbar;
    TextView tvTitle,tvBasicInfo,tvMaintenance;
    ImageView ivBack, ivImage;
    SuggetionAdapter suggetionAdapter;
    ServerPlantDetailsAdapter basicAdapter;
    ServerPlantDetailsAdapter maintenanceAdapter;
    PlantDetailsM plantDetailsM;
    List<String>  basicInfoList = new ArrayList<>();
    List<String>  mainList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        recFeed = (RecyclerView) findViewById(R.id.recFeed);
        recMaintainance = (RecyclerView) findViewById(R.id.recMaintainanace);
        toolbar = (Toolbar) findViewById(R.id.appBar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivImage = (ImageView) findViewById(R.id.ivFlower);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBasicInfo = (TextView) findViewById(R.id.tvBasicInfo);
        tvMaintenance = (TextView) findViewById(R.id.tvMaintainanace);
        Intent i = getIntent();
        if (i.hasExtra(AppConstant.ExtraObject)) {
            plantMasterM = (PlantMasterM) i.getSerializableExtra(AppConstant.ExtraObject);
            recMaintainance.setVisibility(View.GONE);
            tvBasicInfo.setVisibility(View.GONE);
            tvMaintenance.setVisibility(View.GONE);
        } else {
            plantDetailsM = (PlantDetailsM) i.getSerializableExtra(AppConstant.PLANT_DETAILS);
        }
        if (plantMasterM != null) {
            suggetionAdapter = new SuggetionAdapter(plantMasterM.getSuggestions(), PlantDetailsActivity.this);
            tvTitle.setText(plantMasterM.getSuggestions().get(0).getPlant().getName());
            Glide.with(this).load(plantMasterM.getImages().get(0).getUrl()).into(ivImage);
            recFeed.setAdapter(suggetionAdapter);
        } else {
            tvTitle.setText(plantDetailsM.getName());
            Glide.with(this).load(ApiUrl.baseUrl+plantDetailsM.getImage()).into(ivImage);
            getJsonData();
        }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getJsonData() {
        AndroidNetworking.get(ApiUrl.baseUrl + plantDetailsM.getFilePath())
                .setTag(ApiUrl.baseUrl + plantDetailsM.getFilePath())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response",response.toString());
                        ServerPlantMasterM plantListMasterM = new GsonBuilder().create().fromJson(response.toString(), ServerPlantMasterM.class);
                        try {
                            JSONObject basic = response.getJSONObject("basic");
                            Iterator<String> iter = basic.keys(); //This should be the iterator you want.
                            while(iter.hasNext()){
                                String key = iter.next();
                                String value = basic.getString(key);
                                if(value == null || value.isEmpty()){
                                    basicInfoList.add(key+":"+"Not Found");
                                }else{
                                    basicInfoList.add(key+":"+value);
                                }
                            }

                            JSONObject maintanance = response.getJSONObject("maintenance");
                            Iterator<String> main = maintanance.keys(); //This should be the iterator you want.
                            while(main.hasNext()){
                                String key = main.next();
                                String value = maintanance.getString(key);
                                if(value == null || value.isEmpty()){
                                    mainList.add(key+":"+"Not Found");
                                }else{
                                    Log.e("mainValue",key+":"+value);
                                    mainList.add(key+":"+value);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        basicAdapter = new ServerPlantDetailsAdapter(PlantDetailsActivity.this,basicInfoList);
                        recFeed.setAdapter(basicAdapter);
                        maintenanceAdapter = new ServerPlantDetailsAdapter(PlantDetailsActivity.this,mainList);
                        recMaintainance.setAdapter(maintenanceAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        recFeed.setVisibility(View.GONE);
                        Toast.makeText(PlantDetailsActivity.this, "Unable to get Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
