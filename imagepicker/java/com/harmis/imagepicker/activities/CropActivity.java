package com.harmis.imagepicker.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.harmis.imagepicker.R;
import com.harmis.imagepicker.Views.CompressFile;
import com.harmis.imagepicker.cropper.CropImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class CropActivity extends Activity {

    CropImageView cropImageView;
    ImageView iv_crop;
    ImageView iv_done;
    ImageView iv_header;

    File file;
    Uri imageUri;
    String aspectRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_crop );

        cropImageView = findViewById( R.id.cropImageView );
        iv_crop = findViewById( R.id.iv_crop );
        iv_done = findViewById( R.id.iv_done );
        iv_header = findViewById( R.id.iv_header );


        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );

        Log.e( "Image ", getIntent().getData().toString() );
        imageUri = getIntent().getData();
        if (imageUri == null)
            finish();

        //file=new File(imageUri.getPath());
     /*   try{
            System.out.println("Aspect Ratio crop : " + getIntent().getStringExtra("aspectratio"));
            aspectRatio = getIntent().getStringExtra("aspectratio");
            String[] asRatio = aspectRatio.split("x");
            cropImageView.setAspectRatio(Integer.parseInt(asRatio[0]), Integer.parseInt(asRatio[1]));
        }catch (Exception e){
            cropImageView.setAspectRatio(1,1);
        }*/


        cropImageView.setImageUriAsync( imageUri );
        cropImageView.setGuidelines( CropImageView.Guidelines.ON );
        cropImageView.setCropShape( CropImageView.CropShape.RECTANGLE );
        cropImageView.setScaleType( CropImageView.ScaleType.FIT_CENTER );

        cropImageView.setAutoZoomEnabled( true );
        cropImageView.setShowProgressBar( true );
        cropImageView.setBackgroundColor( Color.BLACK );
        cropImageView.setMultiTouchEnabled( false );

        cropImageView.setOnCropImageCompleteListener( new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

                Bitmap bitmap = result.getBitmap();
                OutputStream os = null;
                try {
                    String uuid = UUID.randomUUID().toString();
                    File file = File.createTempFile( uuid, ".jpg", getCacheDir() );
                    FileOutputStream fileOutputStream = new FileOutputStream( file );
                    if (fileOutputStream == null) {
                        Toast.makeText( getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT ).show();
                        finish();
                        return;
                    }
                    os = new BufferedOutputStream( fileOutputStream );
                    if (!bitmap.compress( Bitmap.CompressFormat.JPEG, 90, os ))
                        throw new IOException( "Can't compress photo" );
                    os.flush();
                    file = CompressFile.getCompressedImageFile( file, CropActivity.this );

                    Intent resultIntent = new Intent();
                    resultIntent.setData( Uri.fromFile( file ) );
                    setResult( Activity.RESULT_OK, resultIntent );
                    finish();

                } catch (IOException e) {
                } finally {
                    try {
                        if (os != null) os.close();
                    } catch (IOException ignored) {
                    }
                }
                finish();
            }
        } );

        iv_crop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage( 90 );
            }
        } );

        iv_done.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.getCroppedImageAsync( 512, 512 );
            }
        } );

        iv_header.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        } );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

}
