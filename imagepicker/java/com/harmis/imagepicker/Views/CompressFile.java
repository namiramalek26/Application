package com.harmis.imagepicker.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import com.harmis.imagepicker.activities.CropImageActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class CompressFile {
    public static File getCompressedImageFile(File file, Context mContext) {
        Log.e( "Compress", file.getAbsolutePath().toString() );
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            if (getFileExt( file.getName() ).equals( "png" ) || getFileExt( file.getName() ).equals( "PNG" )) {
                o.inSampleSize = 6;
            } else {
                o.inSampleSize = 6;
            }

            FileInputStream inputStream = new FileInputStream( file );
            BitmapFactory.decodeStream( inputStream, null, o );
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream( file );

            Bitmap selectedBitmap = BitmapFactory.decodeStream( inputStream, null, o2 );

            ExifInterface ei = new ExifInterface( file.getAbsolutePath() );
            int orientation = ei.getAttributeInt( ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED );

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateImage( selectedBitmap, 90 );
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateImage( selectedBitmap, 180 );
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotateImage( selectedBitmap, 270 );
                    break;

                case ExifInterface.ORIENTATION_NORMAL:

                default:
                    break;
            }
            inputStream.close();

            // here i override the original image file
            File folder = new File( Environment.getExternalStorageDirectory() + "/Image_Picker" );
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }/* else {
                folder.delete();
                success = folder.mkdir();
            }*/
            if (success) {
                //File newFile = new File( new File( folder.getAbsolutePath() ), file.getName().replaceAll(" ", "_") );
                String uuid = UUID.randomUUID().toString();
                File newFile = File.createTempFile(uuid, ".jpg", mContext.getCacheDir());
                if (newFile.exists()) {
                    newFile.delete();
                }
                FileOutputStream outputStream = new FileOutputStream( newFile );

                if (getFileExt( file.getName() ).equals( "png" ) || getFileExt( file.getName() ).equals( "PNG" )) {
                    selectedBitmap.compress( Bitmap.CompressFormat.PNG, 100, outputStream );
                } else {
                    selectedBitmap.compress( Bitmap.CompressFormat.JPEG, 100, outputStream );
                }

                return newFile;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileExt(String fileName) {
        return fileName.substring( fileName.lastIndexOf( "." ) + 1, fileName.length() );
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate( angle );
        return Bitmap.createBitmap( source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true );
    }

    public static String compressImage(Activity activity, String imagePath) {
        OutputStream os = null;
        try {
            String uuid = UUID.randomUUID().toString();
            File file = File.createTempFile(uuid, ".jpg", activity.getCacheDir());
            File originalFile = new File(imagePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (fileOutputStream == null) {
                return null;
            }
            os = new BufferedOutputStream(fileOutputStream);
            /*if (!bitmap.compress( Bitmap.CompressFormat.JPEG, 90, os ))
                throw new IOException( "Can't compress photo" );*/
            os.flush();
            file = getCompressedImageFile(originalFile, activity);
            Log.e("TAG", "Normal Image Size : " + originalFile.length());
            Log.e("TAG", "Image Size : " + file.length());
            if (originalFile.length() > file.length())
                return Uri.fromFile(file).toString().replace("file://", "");

        } catch (IOException e) {
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException ignored) {
            }
        }
        return null;
    }

}
