package com.hotapps.easyplant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyHolder> {

    Context context;
    List<PlantDetailsM> plantDetailsMList;
    OnSeachItemClick onSeachItemClick;


    public SearchHistoryAdapter(Context context, List<PlantDetailsM> plantDetailsMList) {
        this.context = context;
        this.plantDetailsMList = plantDetailsMList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_history, null);
        return new MyHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final PlantDetailsM plantDetailsM = plantDetailsMList.get(position);
        holder.tvName.setText(plantDetailsM.getName());
        holder.tvOrigin.setText("Origin : " + plantDetailsM.getOrigin());
        if (plantDetailsM.getImage().startsWith("https")) {
            Glide.with(context).load(plantDetailsM.getImage()).into(holder.ivImage);
        } else {
            Glide.with(context).load(ApiUrl.baseUrl + plantDetailsM.getImage()).into(holder.ivImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSeachItemClick.onItemClick(plantDetailsM);
            }
        });
    }

    @Override
    public int getItemCount() {
        return plantDetailsMList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvName, tvOrigin;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = (AppCompatImageView) itemView.findViewById(R.id.ivImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvOrigin = (TextView) itemView.findViewById(R.id.tvOrigin);
        }
    }


    public interface OnSeachItemClick {
        void onItemClick(PlantDetailsM plantDetailsM);
    }

    public void setCallBack(OnSeachItemClick onSeachItemClick) {
        this.onSeachItemClick = onSeachItemClick;
    }
}
