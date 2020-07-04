package com.hotapps.easyplant.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hotapps.easyplant.R;
import com.hotapps.easyplant.activities.WebViewActivty;
import com.hotapps.easyplant.model.plantDetails.Suggestion;

import java.util.ArrayList;
import java.util.List;

public class SuggetionAdapter extends RecyclerView.Adapter<SuggetionAdapter.MyHolder>{

    List<Suggestion>  mList = new ArrayList<>();
    Activity activity;

    public SuggetionAdapter(List<Suggestion>  suggestionList,Activity activity) {
        this.mList = suggestionList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_suggesions_layout,null);
        return  new MyHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
     final Suggestion suggestion = mList.get(position);
        Log.e("size",mList.size()+"");
     holder.tvName.setText(suggestion.getPlant().getName());
     holder.tvOrigin.setText(suggestion.getPlant().getCommonName() != null ? suggestion.getPlant().getCommonName() : "Not Found");
     holder.tvWikepedia.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent i = new Intent(activity, WebViewActivty.class);
             i.putExtra("url",suggestion.getPlant().getUrl());
             activity.startActivity(i);
         }
     });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  static   class  MyHolder extends  RecyclerView.ViewHolder{
        TextView tvName,tvOrigin,tvWikepedia;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvWikepedia = (TextView)itemView.findViewById(R.id.tvWikipedia);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvOrigin = (TextView)itemView.findViewById(R.id.tvCommonName);
        }
    }

    public  void  addAll(List<Suggestion> list){
        list.addAll(mList);
        notifyDataSetChanged();
    }
}
