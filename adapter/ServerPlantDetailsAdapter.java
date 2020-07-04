package com.hotapps.easyplant.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotapps.easyplant.R;

import java.util.List;

public class ServerPlantDetailsAdapter extends RecyclerView.Adapter<ServerPlantDetailsAdapter.MyHolder> {


    List<String> mList ;
    Activity activity;

    public ServerPlantDetailsAdapter(Activity activity,List<String>  basicInfoList) {
        this.activity = activity;
        this.mList = basicInfoList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_plant_details, null);
        return new MyHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final String suggestion = mList.get(position);
        Log.e("suggesion",suggestion);
        holder.tvName.setText(suggestion.split(":")[0]+"   : ");
        holder.tvOrigin.setText(suggestion.split(":")[1]);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvOrigin;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvOrigin = (TextView) itemView.findViewById(R.id.tvCommonName);
        }
    }


}


