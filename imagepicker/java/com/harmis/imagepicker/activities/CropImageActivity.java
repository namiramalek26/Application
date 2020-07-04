package com.harmis.imagepicker.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.harmis.imagepicker.R;
import com.harmis.imagepicker.Views.CompressFile;
import com.harmis.imagepicker.adapter.CropImageAdapter;
import com.harmis.imagepicker.adapter.ImageListAdapter;
import com.harmis.imagepicker.interfaces.OnImageClickPosition;
import com.harmis.imagepicker.model.Images;
import com.harmis.imagepicker.utils.CommonKeyword;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CropImageActivity extends AppCompatActivity implements OnImageClickPosition {

    List<String> imageList = new ArrayList<>();
    List<Images> imageLists = new ArrayList<>();
    ViewPager viewPager;
    CropImageAdapter cropImageAdapter;
    ImageListAdapter imagesAdapter;
    RecyclerView recyclerImages;
    int REQ_CODE_CROP_PHOTO = 101;
    boolean isWarningShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        viewPager = findViewById(R.id.viewPager);
        recyclerImages = findViewById(R.id.recyclerImages);

        imageList = (List<String>) getIntent().getSerializableExtra(CommonKeyword.RESULT);

        imageLists.clear();
        for (int i = 0; i < imageList.size(); i++) {
            Images images = new Images();
            images.setChecked(false);
            if (i == 0)
                images.setChecked(true);
            images.setImageUrl(imageList.get(i));

            imageLists.add(images);
        }

        cropImageAdapter = new CropImageAdapter(this, imageLists, this);
        viewPager.setAdapter(cropImageAdapter);
        viewPager.setOffscreenPageLimit(0);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        imagesAdapter = new ImageListAdapter(this, imageLists, this);
        recyclerImages.setLayoutManager(layoutManager);
        recyclerImages.setHasFixedSize(false);
        recyclerImages.setAdapter(imagesAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageList();
                imageLists.get(position).setChecked(true);
                imagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void setImageList() {
        for (int i = 0; i < imageList.size(); i++) {
            imageLists.get(i).setChecked(false);
        }
    }

    @Override
    public void onImageClickPosition(int position, boolean isChecked) {
        setImageList();
        viewPager.setCurrentItem(position);
        imageLists.get(position).setChecked(isChecked);
        cropImageAdapter.notifyDataSetChanged();
        imagesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_CROP_PHOTO) {
            if (resultCode == CropActivity.RESULT_OK) {
                imageLists.get(viewPager.getCurrentItem()).setImageUrl(data.getData().toString().
                        replace("file://", ""));
                cropImageAdapter.notifyDataSetChanged();
                imagesAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crop, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_crop) {
            Log.e("log", "path : " + imageList.get(viewPager.getCurrentItem()));
            Intent cropImageIntent = new Intent(null, Uri.fromFile(new File(imageList.get(viewPager.getCurrentItem()))), this, CropActivity.class);
            startActivityForResult(cropImageIntent, REQ_CODE_CROP_PHOTO);
            // Toast.makeText(getApplicationContext(), "Crop", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_done) {
            for (int i = 0; i < imageLists.size(); i++) {
                compressImage(imageLists.get(i).getImageUrl(), i);
            }
            Intent intent = new Intent();
            intent.putExtra(CommonKeyword.RESULT, (Serializable) imageLists);
            setResult(CommonKeyword.RESULT_CODE_CROP_IMAGE, intent);
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void compressImage(String imagePath, int position) {
        OutputStream os = null;
        try {
            String uuid = UUID.randomUUID().toString();
            File file = File.createTempFile(uuid, ".jpg", getCacheDir());
            File originalFile = new File(imagePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (fileOutputStream == null) {
                Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            os = new BufferedOutputStream(fileOutputStream);
            /*if (!bitmap.compress( Bitmap.CompressFormat.JPEG, 90, os ))
                throw new IOException( "Can't compress photo" );*/
            os.flush();
            file = CompressFile.getCompressedImageFile(originalFile, CropImageActivity.this);
            Log.e("TAG", "Normal Image Size : " + originalFile.length());
            Log.e("TAG", "Image Size : " + file.length());
            if (originalFile.length() > file.length())
                imageLists.get(position).setImageUrl(Uri.fromFile(file).toString().replace("file://", ""));
            if (file.length() > 5000000 && !isWarningShowed) {
                isWarningShowed = true;
                Toast.makeText(CropImageActivity.this, "Document size must be less than 5 MB", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException ignored) {
            }
        }
    }
}
