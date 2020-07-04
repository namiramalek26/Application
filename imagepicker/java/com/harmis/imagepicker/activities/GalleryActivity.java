package com.harmis.imagepicker.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.harmis.imagepicker.R;
import com.harmis.imagepicker.adapter.ImagesAdapter;
import com.harmis.imagepicker.interfaces.OnImageClickPosition;
import com.harmis.imagepicker.model.Images;
import com.harmis.imagepicker.utils.CommonKeyword;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements OnImageClickPosition {

    RecyclerView recyclerImages;
    ImagesAdapter imagesAdapter;
    int count = 0;
    int max = 1;
    TextView tvTotalImages;
    private ArrayList<Images> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_gallery );

        //getSupportActionBar().setTitle(Html.fromHtml("<font color='#ff0000'>Gallery</font>") + "tittle");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            max = bundle.getInt( CommonKeyword.MAX_IMAGES );
        }
        tvTotalImages = findViewById( R.id.tvTotalImages );
        recyclerImages = findViewById( R.id.recyclerImages );
        imagesAdapter = new ImagesAdapter( this, images, this );
        GridLayoutManager layoutManager;
        layoutManager = new GridLayoutManager( GalleryActivity.this, 3 ) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller( GalleryActivity.this ) {

                    private static final float SPEED = 1200f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }

                };
                smoothScroller.setTargetPosition( position );
                startSmoothScroll( smoothScroller );
            }

        };
        recyclerImages.setLayoutManager( layoutManager );
        recyclerImages.setHasFixedSize( false );
        recyclerImages.setAdapter( imagesAdapter );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE )
                    == PackageManager.PERMISSION_GRANTED) {
                getAllShownImagesPath( this );
            } else {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1 );
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            getAllShownImagesPath( this );
        }
    }

    private void getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String orderBy = MediaStore.Images.Media._ID + " DESC";
        cursor = activity.getContentResolver().query( uri, projection, null,
                null, orderBy );

        column_index_data = cursor.getColumnIndexOrThrow( MediaStore.MediaColumns.DATA );
        column_index_folder_name = cursor
                .getColumnIndexOrThrow( MediaStore.Images.Media.BUCKET_DISPLAY_NAME );
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString( column_index_data );

            Images image = new Images();
            image.setChecked( false );
            image.setImageUrl( absolutePathOfImage );
            images.add( image );
        }

        imagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            getAllShownImagesPath( this );
            //resume tasks needing this permission
        }
    }

    @Override
    public void onImageClickPosition(int position, boolean isChecked) {
        if (isChecked) {
            if (count >= max) {
                tvTotalImages.setText( "You can select Max " + max + " images." );
                return;
            } else {
                if (isChecked)
                    count++;
                else
                    count--;
            }
        } else {
            if (isChecked)
                count++;
            else
                count--;
        }
        images.get( position ).setChecked( isChecked );
        imagesAdapter.notifyItemChanged( position );
        tvTotalImages.setText( count + " Images selected" );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_photo, menu );

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            List<String> selectedImages = new ArrayList<>();

            if (images != null && images.size() > 0) {
                for (Images images : images)
                    if (images.isChecked()) {
                        selectedImages.add( images.getImageUrl() );
                        Log.e( "Selected image ", images.getImageUrl().toString() );
                    }

                Intent intent = new Intent( new Intent( getApplicationContext(), CropImageActivity.class ) );
                intent.putExtra( CommonKeyword.RESULT, (Serializable) selectedImages );
                startActivityForResult( intent, CommonKeyword.REQUEST_CODE );
            } else {
                Toast.makeText( GalleryActivity.this, "Please select at least one image",
                        Toast.LENGTH_LONG ).show();
            }
        } else {
            return super.onOptionsItemSelected( item );
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == CommonKeyword.RESULT_CODE_CROP_IMAGE) {
            if (requestCode == CommonKeyword.REQUEST_CODE) {
                List<Images> images = (List<Images>) data.getSerializableExtra( CommonKeyword.RESULT );
                Intent intent = new Intent();
                intent.putExtra( CommonKeyword.RESULT, (Serializable) images );
                setResult( CommonKeyword.RESULT_CODE_GALLERY, intent );
                finish();
            }
        }
    }
}
