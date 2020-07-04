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
import com.hotapps.easyplant.activities.PlantDetailsActivity;
import com.hotapps.easyplant.activities.SearchPlantActivity;
import com.hotapps.easyplant.adapter.FeedAdapter;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;
import com.hotapps.easyplant.model.plantList.PlantListMasterM;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentFeed extends Fragment {


    RecyclerView recFeed;
    List<PlantDetailsM> plantList = new ArrayList<>();
    FeedAdapter feedAdapter;
    ProgressBar progressBar;
    TextView tvNoDataFound;
    TextView tvSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_feed, container, false);
        recFeed = (RecyclerView) view.findViewById(R.id.recFeed);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tvNoDataFound = (TextView) view.findViewById(R.id.tvDataFound);
        tvSearch = (TextView) view.findViewById(R.id.tvSearch);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SearchPlantActivity.class);
                startActivity(i);
            }
        });
        feedAdapter = new FeedAdapter(plantList);
        recFeed.setAdapter(feedAdapter);
        if (CommonUtils.isNetworkConnected(getActivity())) {
            getAllFlower();
        } else {
            Toast.makeText(getActivity(), "No Internet Connected", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void getAllFlower() {
        progressBar.setVisibility(View.VISIBLE);
        recFeed.setVisibility(View.GONE);
        AndroidNetworking.get(ApiUrl.getAllFlower)
                .setTag(ApiUrl.getAllFlower)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        recFeed.setVisibility(View.VISIBLE);
                        plantList.clear();
                        PlantListMasterM plantListMasterM = new GsonBuilder().create().fromJson(response.toString(), PlantListMasterM.class);
                        if (plantListMasterM.getSearchList() != null && plantListMasterM.getSearchList().size() > 0) {
                            tvNoDataFound.setVisibility(View.GONE);
                            recFeed.setVisibility(View.VISIBLE);
                            plantList.addAll(plantListMasterM.getSearchList());
                            feedAdapter.notifyDataSetChanged();
                            feedAdapter.setCallback(new FeedAdapter.OnFeedItemClickListener() {
                                @Override
                                public void onFeedItemClick(PlantDetailsM plantDetailsM) {
                                    Intent i = new Intent(getActivity(), PlantDetailsActivity.class);
                                    i.putExtra(AppConstant.PLANT_DETAILS, plantDetailsM);
                                    startActivity(i);
                                }
                            });

                        } else {
                            tvNoDataFound.setVisibility(View.VISIBLE);
                            recFeed.setVisibility(View.GONE);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        recFeed.setVisibility(View.GONE);
                        tvNoDataFound.setVisibility(View.VISIBLE);

                    }
                });
    }


}
