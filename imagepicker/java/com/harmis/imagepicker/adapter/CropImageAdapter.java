package com.harmis.imagepicker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.harmis.imagepicker.R;
import com.harmis.imagepicker.interfaces.OnImageClickPosition;
import com.harmis.imagepicker.model.Images;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paras Andani
 */
public class CropImageAdapter extends PagerAdapter {

    List<Images> imageList = new ArrayList<>();
    Activity activity;
    OnImageClickPosition onImageClickPosition;

    public CropImageAdapter(Activity activity, List<Images> imageList, OnImageClickPosition onImageClickPosition) {
        this.activity = activity;
        this.imageList = imageList;
        this.onImageClickPosition = onImageClickPosition;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView( (LinearLayout) object );
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from( activity );
        View itemView = inflater.inflate( R.layout.layout_pager_images, container, false );

        ImageView imageView = itemView.findViewById( R.id.ivImages );
        Glide.with( activity ).load( imageList.get( position ).getImageUrl() )
                .into( imageView );

        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClickPosition.onImageClickPosition( position, true );
            }
        } );

        container.addView( itemView );

        return itemView;
    }
}
