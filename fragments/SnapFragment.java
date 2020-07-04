package com.hotapps.easyplant.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.harmis.imagepicker.CameraUtils.CameraIntentHelper;
import com.harmis.imagepicker.CameraUtils.CameraIntentHelperCallback;
import com.harmis.imagepicker.activities.CropImageActivity;
import com.harmis.imagepicker.activities.GalleryActivity;
import com.harmis.imagepicker.model.Images;
import com.harmis.imagepicker.utils.CommonKeyword;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.activities.PlantDetailsActivity;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.AppConstant;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.plantDetails.PlantMasterM;
import com.hotapps.easyplant.model.plantList.PlantDetailsM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SnapFragment extends Fragment {


    private RelativeLayout relCamera, relGallery;
    private CameraIntentHelper mCameraIntentHelper;
    private ImageView ivSelectedPhoto;
    private int whichImage = 1;
    private static final int UPLOAD_IMAGE_SIZE = 1024;
    private Handler handler = new Handler();
    private int identificationId;
    LoginDetailsM loginDetailsM;
    ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginDetailsM = new PreManager(getActivity()).getLoginDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(progressBar != null){
            if(progressBar.getVisibility() == View.VISIBLE){
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snap, container, false);
        relCamera = view.findViewById(R.id.rel_camera);
        relGallery = view.findViewById(R.id.rel_gallery);
        ivSelectedPhoto = view.findViewById(R.id.ivPickedImage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mCameraIntentHelper = getCameraIntentHeleper();
        relCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraIntentHelper.startCameraIntent(whichImage);
            }
        });

        relGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        return view;
    }

    private CameraIntentHelper getCameraIntentHeleper() {
        mCameraIntentHelper = new CameraIntentHelper(getActivity(), new CameraIntentHelperCallback() {
            @Override
            public void onPhotoUriFound(int requestCode, Date dateCameraIntentStarted, Uri photoUri, int rotateXDegrees) {
                List<String> imagesList = new ArrayList<>();
                imagesList.add(photoUri.getPath());
                Intent intent = new Intent(new Intent(getActivity(), CropImageActivity.class));
                intent.putExtra(CommonKeyword.RESULT, (Serializable) imagesList);
                startActivityForResult(intent, whichImage);
            }

            @Override
            public void deletePhotoWithUri(Uri photoUri) {

            }

            @Override
            public void onSdCardNotMounted() {

            }

            @Override
            public void onCanceled() {

            }

            @Override
            public void onCouldNotTakePhoto() {

            }

            @Override
            public void onPhotoUriNotFound(int requestCode) {

            }

            @Override
            public void logException(Exception e) {
                Log.e("log_tag", "log Exception : " + e.getMessage());
            }
        });

        return mCameraIntentHelper;
    }


    private void openGallery() {
        Intent intent = new Intent(getActivity(), GalleryActivity.class);
        intent.putExtra(CommonKeyword.MAX_IMAGES, 1);
        startActivityForResult(intent, CommonKeyword.REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCameraIntentHelper.onActivityResult(requestCode, resultCode, data);
        Log.e("called", "called");
        if (requestCode == CommonKeyword.REQUEST_CODE_GALLERY && resultCode == CommonKeyword.RESULT_CODE_GALLERY) {
            List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
            if (imagesList.size() > 0) {
                Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivSelectedPhoto);
            }
            List<String> imageList = new ArrayList<>();
            imageList.add(imagesList.get(0).getImageUrl());
            getStingImageData(imageList);


        } else if (requestCode == CommonKeyword.REQUEST_CODE_CAMERA && resultCode == CommonKeyword.RESULT_CODE_CAMERA) {
            List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
            if (imagesList != null && imagesList.size() > 0) {
                Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivSelectedPhoto);
            }
            List<String> imageList = new ArrayList<>();
            imageList.add(imagesList.get(0).getImageUrl());
            getStingImageData(imageList);

        } else if (resultCode == CommonKeyword.RESULT_CODE_CROP_IMAGE) {
            if (requestCode == whichImage) {
                List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
                if (imagesList != null && imagesList.size() > 0)
                    Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivSelectedPhoto);
                List<String> imageList = new ArrayList<>();
                imageList.add(imagesList.get(0).getImageUrl());
                getStingImageData(imageList);
            }
        }


    }

    void delayApi(int id) {
        identificationId = id;
        handler.post(checkIdentification);  // start periodic check of identification result
    }

    private void getIdOfFlower(JSONObject data) {
        CommonUtils.show(getActivity());
        AndroidNetworking.post(ApiUrl.getIdUrl)
                .addJSONObjectBody(data)
                .setTag(ApiUrl.login)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        CommonUtils.hide();
                        Log.e("identification_response", response.toString());
                        try {
                            JSONObject json = new JSONObject(new JSONTokener(response));
                            delayApi(json.getInt("id"));
                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        CommonUtils.hide();
                        Log.e("identification_response", anError.getMessage());
                    }
                });
    }

    public void getStingImageData(List<String> imagePaths) {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject data = new JSONObject();
        JSONArray images = new JSONArray();
        for (String imagePath : imagePaths) {
            Bitmap bitmap = CommonUtils.decodeSampledBitmapFromPath(imagePath, UPLOAD_IMAGE_SIZE, UPLOAD_IMAGE_SIZE);
            images.put(CommonUtils.getStringImage(bitmap));
        }
        try {
            data.put(ApiParams.api_key, AppConstant.plantApiKey);
            data.put(ApiParams.images, images);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("params", data.toString());
        getIdOfFlower(data);
    }


    private void getCheckIdentifyApi(int id) {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject data = new JSONObject();
            JSONArray ids = new JSONArray();
            ids.put(id);
            data.put(ApiParams.api_key, AppConstant.plantApiKey);
            data.put(ApiParams.ids, ids);
            JsonParser parser = new JsonParser();
            gsonObject = (JsonObject) parser.parse(data.toString());
        } catch (JSONException error) {
            error.printStackTrace();
        }
        Log.e("check params", new Gson().toJson(gsonObject));
        checkIdentity(gsonObject);
    }

    private void checkIdentity(JsonObject data) {
        CommonUtils.show(getActivity());
        AndroidNetworking.post(ApiUrl.chekIdentification)
                .setContentType("Application/json")
                .addStringBody(new Gson().toJson(data))
                .setTag(ApiUrl.login)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        CommonUtils.hide();
                        PlantMasterM plantMasterM = null;
                        try {
                            plantMasterM = new GsonBuilder().create().fromJson(response.getJSONObject(0).toString(), PlantMasterM.class);
                            if (plantMasterM.getSuggestions() != null) {
                                if (plantMasterM.getSuggestions().size() > 0) {
                                    removeHandlerCallback();
                                    PlantDetailsM plantDetailsM = new PlantDetailsM();
                                    plantDetailsM.setName(plantMasterM.getSuggestions().get(0).getPlant().getName());
                                    plantDetailsM.setFilePath(response.toString());
                                    plantDetailsM.setOrigin("Not Found");
                                    plantDetailsM.setImage(plantMasterM.getImages().get(0).getUrl());
                                    addSerchHistory(plantDetailsM, plantMasterM);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("exception", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        CommonUtils.hide();
                        Log.e("flower_response", anError.getMessage());
                    }
                });
    }


    private Runnable checkIdentification = new Runnable() {
        @Override
        public void run() {
            getCheckIdentifyApi(identificationId);
            handler.postDelayed(this, 2000);
        }
    };

    void removeHandlerCallback() {
        handler.removeCallbacks(checkIdentification);  // stop periodic check
    }


    private void addSerchHistory(final PlantDetailsM plantDetailsM, final PlantMasterM plantMasterM) {
        CommonUtils.show(getActivity());
        AndroidNetworking.post(ApiUrl.addSearchHistory)
                .addBodyParameter(ApiParams.name, plantDetailsM.getName())
                .addBodyParameter(ApiParams.origin, plantDetailsM.getOrigin())
                .addBodyParameter(ApiParams.image, plantDetailsM.getImage())
                .addBodyParameter(ApiParams.json_data, plantDetailsM.getFilePath())
                .addBodyParameter(ApiParams.userId, loginDetailsM.getId())
                .setTag(ApiUrl.addSearchHistory)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.hide();
                        progressBar.setVisibility(View.VISIBLE);
                        Log.e("response", response.toString());
                        Intent i = new Intent(getActivity(), PlantDetailsActivity.class);
                        i.putExtra(AppConstant.ExtraObject, (Serializable) plantMasterM);
                        i.putExtra("data", response.toString());
                        startActivity(i);

                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.VISIBLE);
                        CommonUtils.hide();
                        Toast.makeText(getActivity(), "Unable to get Data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void delayRedirection(final PlantMasterM plantMasterM, final String response) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);
    }


}
