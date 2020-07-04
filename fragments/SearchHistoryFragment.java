package com.hotapps.easyplant.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.activities.PlantDetailsActivity;
import com.hotapps.easyplant.adapter.SearchHistoryAdapter;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.plantDetails.PlantMasterM;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;
import com.hotapps.easyplant.model.plantList.PlantListMasterM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SearchHistoryFragment extends Fragment {

    SearchHistoryAdapter searchHistoryAdapter;
    RecyclerView recyclerView;
    TextView tvNoHistory;
    List<PlantDetailsM>  plantDetailsMList = new ArrayList<>();
    LoginDetailsM loginDetailsM;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);
        recyclerView =(RecyclerView)view.findViewById(R.id.recFeed);
        tvNoHistory =(TextView)view.findViewById(R.id.tvNoHistory);
        progressBar =(ProgressBar)view.findViewById(R.id.progressBar);
        loginDetailsM = new PreManager(getActivity()).getLoginDetails();
        setUpAdapter();


        if(CommonUtils.isNetworkConnected(getActivity())){
            getSearchHistoryApi();
        }else {
            Toast.makeText(getActivity(), "No Internet Connected", Toast.LENGTH_SHORT).show();
        }
        return  view;
    }
    private void  setUpAdapter(){
        searchHistoryAdapter = new SearchHistoryAdapter(getActivity(),plantDetailsMList);
        recyclerView.setAdapter(searchHistoryAdapter);

    }
    private void getSearchHistoryApi() {
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.post(ApiUrl.getSearchHistory)
                .addBodyParameter(ApiParams.userId,loginDetailsM.getId())
                .setTag(ApiUrl.getSearchHistory)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        PlantListMasterM plantListMasterM = new GsonBuilder().create().fromJson(response.toString(), PlantListMasterM.class);
                        if(plantListMasterM.getSuccess() == 1){
                            recyclerView.setVisibility(View.VISIBLE);
                            tvNoHistory.setVisibility(View.GONE);
                            plantDetailsMList.addAll(plantListMasterM.getSearchList());
                            searchHistoryAdapter.notifyDataSetChanged();
                            searchHistoryAdapter.setCallBack(new SearchHistoryAdapter.OnSeachItemClick() {
                                @Override
                                public void onItemClick(PlantDetailsM plantDetailsM) {
                                    if(plantDetailsM.getImage().startsWith("https")){
                                        try {
                                            JSONArray jsonObject = new JSONArray(plantDetailsM.getFilePath());
                                            PlantMasterM plantMasterM =  new GsonBuilder().create().fromJson(jsonObject.getJSONObject(0).toString(),PlantMasterM.class);
                                            Intent i = new Intent(getActivity(), PlantDetailsActivity.class);
                                            i.putExtra(AppConstant.ExtraObject,(Serializable) plantMasterM);
                                            i.putExtra("data", plantDetailsM.getFilePath());
                                            startActivity(i);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }else{
                                        Intent i = new Intent(getActivity(), PlantDetailsActivity.class);
                                        i.putExtra(AppConstant.PLANT_DETAILS,plantDetailsM);
                                        startActivity(i);
                                    }

                                }
                            });
                        }else{
                            recyclerView.setVisibility(View.GONE);
                            tvNoHistory.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        tvNoHistory.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Unable to get Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
