package com.hotapps.easyplant.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.adapter.SearchHistoryAdapter;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;
import com.hotapps.easyplant.model.plantList.PlantListMasterM;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPlantActivity extends AppCompatActivity {
    SearchHistoryAdapter searchHistoryAdapter;
    RecyclerView recyclerView;
    List<PlantDetailsM> plantDetailsMList = new ArrayList<>();
    LoginDetailsM loginDetailsM;
    ProgressBar progressBar;
    ImageView ivBack;
    TextView tvClear;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_plant);
        recyclerView = (RecyclerView) findViewById(R.id.recFeed);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvClear = (TextView) findViewById(R.id.tvClear);
        etSearch = (EditText) findViewById(R.id.etSearch);
        loginDetailsM = new PreManager(SearchPlantActivity.this).getLoginDetails();
    //    setUpAdapter();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    if (CommonUtils.isNetworkConnected(SearchPlantActivity.this)) {
                        searchPlantApi(s.toString());
                    } else {
                        Toast.makeText(SearchPlantActivity.this, "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    private void searchPlantApi(String keyWord) {
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(ApiUrl.searchPlant)
                .addBodyParameter(ApiParams.keyword, keyWord)
                .setTag(ApiUrl.searchPlant)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response",response.toString());
                        progressBar.setVisibility(View.GONE);
                        plantDetailsMList.clear();
                        PlantListMasterM plantListMasterM = new GsonBuilder().create().fromJson(response.toString(), PlantListMasterM.class);
                        if (plantListMasterM.getSuccess() == 1) {
                            if (plantListMasterM.getSearchList() != null && plantListMasterM.getSearchList().size() > 0) {
                                plantDetailsMList.addAll(plantListMasterM.getSearchList());
                            }

                            searchHistoryAdapter = new SearchHistoryAdapter(SearchPlantActivity.this, plantDetailsMList);
                            recyclerView.setAdapter(searchHistoryAdapter);
                            searchHistoryAdapter.setCallBack(new SearchHistoryAdapter.OnSeachItemClick() {
                                @Override
                                public void onItemClick(PlantDetailsM plantDetailsM) {
                                    addSerchHistory(plantDetailsM);
                                }
                            });

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(SearchPlantActivity.this, "Unable to get Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addSerchHistory(final PlantDetailsM plantDetailsM) {
        CommonUtils.show(SearchPlantActivity.this);
        AndroidNetworking.post(ApiUrl.addSearchHistory)
                .addBodyParameter(ApiParams.name, plantDetailsM.getName())
                .addBodyParameter(ApiParams.origin, plantDetailsM.getOrigin())
                .addBodyParameter(ApiParams.image, plantDetailsM.getImage())
                .addBodyParameter(ApiParams.json_data, plantDetailsM.getFilePath())
                .addBodyParameter(ApiParams.userId, loginDetailsM.getId())
                .setTag(ApiUrl.addSearchHistory)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.hide();
                        Log.e("response",response.toString());
                        Intent i = new Intent(SearchPlantActivity.this, PlantDetailsActivity.class);
                        i.putExtra(AppConstant.PLANT_DETAILS,plantDetailsM);
                        startActivity(i);

                    }

                    @Override
                    public void onError(ANError anError) {
                        CommonUtils.hide();
                        Toast.makeText(SearchPlantActivity.this, "Unable to get Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
