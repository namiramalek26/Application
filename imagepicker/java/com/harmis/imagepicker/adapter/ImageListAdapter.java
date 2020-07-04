package com.harmis.imagepicker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.harmis.imagepicker.R;
import com.harmis.imagepicker.interfaces.OnImageClickPosition;
import com.harmis.imagepicker.model.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paras Andani
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImagesHolder> {

    List<Images> images = new ArrayList<>();
    Activity activity;
    OnImageClickPosition onImageClickPosition;

    public ImageListAdapter(Activity activity, List<Images> images, OnImageClickPosition onImageClickPosition) {
        this.activity = activity;
        this.images = images;
        this.onImageClickPosition = onImageClickPosition;
    }

    @NonNull
    @Override
    public ImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.layout_images_adapter, parent, false );
        return new ImagesHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesHolder holder, final int position) {

        Glide.with( activity ).load( images.get( position ).getImageUrl() )
                .into( holder.ivImages );

        holder.ivImages.setAlpha( 1f );
        holder.ivChecked.setVisibility( View.GONE );
        holder.ivTransparent.setVisibility( View.GONE );

        holder.ivImages.setPadding( 5, 5, 5, 5 );

        if (images.get( position ).isChecked()) {
            holder.ivImages.setBackground( activity.getResources().getDrawable( R.drawable.bg_blue_round_sqaure ) );
        } else {
            holder.ivImages.setBackground( activity.getResources().getDrawable( R.drawable.bg_black_round_sqaure ) );
        }

        holder.frmRoot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickPosition.onImageClickPosition( position, true );
            }
        } );
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImagesHolder extends RecyclerView.ViewHolder {

        ImageView ivImages;
        ImageView ivChecked, ivTransparent;
        FrameLayout frmRoot;

        public ImagesHolder(View itemView) {
            super( itemView );

            ivImages = itemView.findViewById( R.id.ivImages );
            ivChecked = itemView.findViewById( R.id.ivChecked );
            ivTransparent = itemView.findViewById( R.id.ivTransparent );

            frmRoot = itemView.findViewById( R.id.frmRoot );

        }
    }

}
