package com.hotapps.easyplant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;

import java.util.List;

public class FeedAdapter  extends RecyclerView.Adapter<FeedAdapter.MyHolder> {
    List<PlantDetailsM>  plantDetailsMList;
    Context  context;
    OnFeedItemClickListener onFeedItemClickListener;


    public FeedAdapter(List<PlantDetailsM> plantDetailsMList) {
        this.plantDetailsMList = plantDetailsMList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View  itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed_layout,null);
        return  new MyHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final PlantDetailsM plantDetailsM = plantDetailsMList.get(position);
        Glide.with(context).load(ApiUrl.baseUrl+plantDetailsM.getImage()).into(holder.ivImage);
        holder.tvOrigin.setText(plantDetailsM.getOrigin());
        holder.tvName.setText(plantDetailsM.getName());
        holder.tvDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFeedItemClickListener.onFeedItemClick(plantDetailsM);
            }
        });

    }

    @Override
    public int getItemCount() {
        return plantDetailsMList.size();
    }

    public  static   class  MyHolder extends  RecyclerView.ViewHolder{

        AppCompatImageView ivImage;
        TextView tvName,tvDetails,tvOrigin;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = (AppCompatImageView)itemView.findViewById(R.id.ivImage);
            tvDetails = (TextView)itemView.findViewById(R.id.tvDetails);
            tvOrigin = (TextView)itemView.findViewById(R.id.tvOrigin);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
        }
    }

    public  interface  OnFeedItemClickListener{
        void  onFeedItemClick(PlantDetailsM plantDetailsM);
    }

    public void  setCallback(OnFeedItemClickListener onFeedItemClickListener){
        this.onFeedItemClickListener = onFeedItemClickListener;
    }
}
