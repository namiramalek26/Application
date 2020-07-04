package com.hotapps.easyplant.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.harmis.imagepicker.CameraUtils.CameraIntentHelper;
import com.harmis.imagepicker.CameraUtils.CameraIntentHelperCallback;
import com.harmis.imagepicker.activities.CropImageActivity;
import com.harmis.imagepicker.activities.GalleryActivity;
import com.harmis.imagepicker.model.Images;
import com.harmis.imagepicker.utils.CommonKeyword;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.ApiParams;
import com.hotapps.easyplant.common.ApiUrl;
import com.hotapps.easyplant.common.CommonUtils;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.model.LoginDetailsM;
import com.hotapps.easyplant.model.LoginMasterM;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProfileFragment extends Fragment implements BottomSheetFragment.ItemClickListener {

    LoginDetailsM loginDetailsM;
    PreManager preManager;
    ImageView ivProfilePic;
    RelativeLayout relChoose;
    private ProgressBar progressBar;
    Button btnSave;
    private EditText etEmail, etName, etPhone, etPassword, etConfirmPassword;
    CameraIntentHelper mCameraIntentHelper;
    int whichImage = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preManager = new PreManager(getActivity());
        loginDetailsM = preManager.getLoginDetails();
        mCameraIntentHelper = getCameraIntentHeleper();

    }

    public CameraIntentHelper getCameraIntentHeleper() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etName = (EditText) view.findViewById(R.id.etName);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        etConfirmPassword = (EditText) view.findViewById(R.id.etConfirmPassword);
        ivProfilePic = (ImageView) view.findViewById(R.id.profile_image);
        relChoose = (RelativeLayout) view.findViewById(R.id.rel_camera);
        progressBar = (ProgressBar) view.findViewById(R.id.progresBar);
        btnSave = (Button) view.findViewById(R.id.btnSave);


        if (loginDetailsM != null) {
            etEmail.setText(loginDetailsM.getUserEmail());
            etName.setText(loginDetailsM.getUserName());
            etPhone.setText(loginDetailsM.getUserMobile());
            if (loginDetailsM.getProfileImage() != null) {
                Glide.with(container.getContext()).
                        load(ApiUrl.baseUrl + loginDetailsM.getProfileImage()).
                        placeholder(R.drawable.img_user).
                        into(ivProfilePic);
            }
        }
        relChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.isNetworkConnected(getActivity())) {
                    if(checkValidation()){
                        upadetProfile();
                    }
                } else {
                    Toast.makeText(getActivity(), "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;
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
        if (requestCode == CommonKeyword.REQUEST_CODE_GALLERY && resultCode == CommonKeyword.RESULT_CODE_GALLERY) {
            List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
            if (imagesList != null && imagesList.size() > 0) {
                Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivProfilePic);
            }
            progressBar.setMax(Integer.parseInt(String.valueOf(new File(imagesList.get(0).getImageUrl()).length())));
            if (CommonUtils.isNetworkConnected(getActivity())) {
                updateProfilePic(new File(imagesList.get(0).getImageUrl()));

            } else {
                Toast.makeText(getActivity(), "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == CommonKeyword.REQUEST_CODE_CAMERA && resultCode == CommonKeyword.RESULT_CODE_CAMERA) {
            List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
            if (imagesList != null && imagesList.size() > 0) {
                Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivProfilePic);
            }
            progressBar.setMax(Integer.parseInt(String.valueOf(new File(imagesList.get(0).getImageUrl()).length())));
            if (CommonUtils.isNetworkConnected(getActivity())) {
                updateProfilePic(new File(imagesList.get(0).getImageUrl()));
            } else {
                Toast.makeText(getActivity(), "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == whichImage && resultCode == CommonKeyword.RESULT_CODE_CROP_IMAGE) {
            List<Images> imagesList = (List<Images>) data.getSerializableExtra(CommonKeyword.RESULT);
            if (imagesList != null && imagesList.size() > 0)
                Glide.with(getActivity()).load(imagesList.get(0).getImageUrl()).into(ivProfilePic);
            progressBar.setMax(Integer.parseInt(String.valueOf(new File(imagesList.get(0).getImageUrl()).length())));
            if (CommonUtils.isNetworkConnected(getActivity())) {
                updateProfilePic(new File(imagesList.get(0).getImageUrl()));
            } else {
                Toast.makeText(getActivity(), "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();

            }


        }


    }

    public void showBottomSheet() {
        BottomSheetFragment addPhotoBottomDialogFragment = BottomSheetFragment.newInstance();
        addPhotoBottomDialogFragment.setCallBack(this);
        addPhotoBottomDialogFragment.show(getChildFragmentManager(),
                BottomSheetFragment.TAG);
    }


    @Override
    public void onItemClick(String item) {
        if (item.equals(getString(R.string.gallery))) {
            openGallery();
        } else if (item.equals(getString(R.string.camera))) {
            mCameraIntentHelper.startCameraIntent(whichImage);
        }


    }

    private void updateProfilePic(File file) {
        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.upload(ApiUrl.updateProfilePicture)
                .addMultipartFile(ApiParams.image, file)
                .addMultipartParameter(ApiParams.userId, loginDetailsM.getId())
                .setTag(ApiUrl.updateProfilePicture)
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {

                        progressBar.setProgress(Integer.parseInt(String.valueOf(bytesUploaded)));

                        //Log.e("bytes", to"" + bytesUploaded);
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        LoginMasterM loginMasterM = new GsonBuilder().create().fromJson(response.toString(), LoginMasterM.class);
                        preManager.setLoginDetails(loginMasterM.getResult());
                        Glide.with(getActivity()).load(ApiUrl.baseUrl + loginMasterM.getResult().getProfileImage()).into(ivProfilePic);
                        Log.e("response", response.toString());
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        progressBar.setVisibility(View.GONE);

                        // handle error
                        Log.e("error", error.getMessage());

                    }
                });
    }

    private void upadetProfile() {
        CommonUtils.show(getActivity());

        ANRequest.PostRequestBuilder anPostRequestBuilder = new ANRequest.PostRequestBuilder(ApiUrl.updateProfile);
        if (!etPassword.getText().toString().isEmpty()) {
            anPostRequestBuilder.addBodyParameter(ApiParams.password, etPassword.getText().toString().trim());
        }
        anPostRequestBuilder.addBodyParameter(ApiParams.userName, etName.getText().toString().trim())
                .addBodyParameter(ApiParams.mobile, etPhone.getText().toString().trim())
                .addBodyParameter(ApiParams.userId, loginDetailsM.getId())
                .addBodyParameter(ApiParams.token, "56451564654464654")
                .setTag(ApiUrl.login)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CommonUtils.hide();
                        LoginMasterM loginMasterM = new GsonBuilder().create().fromJson(response.toString(), LoginMasterM.class);
                        if (loginMasterM.getSuccess() == 1) {
                            preManager.setLoginDetails(loginMasterM.getResult());
                            Toast.makeText(getActivity(), "" + loginMasterM.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "" + loginMasterM.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e("response", response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        CommonUtils.hide();
                        Log.e("Error", anError.getMessage());


                    }
                });
    }

    private boolean checkValidation() {
        if (etName.getText().toString().isEmpty()) {
            etName.setError(getString(R.string.please_enter_username));
            return false;
        } else if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.please_enter_email));
            return false;
        } else if (!CommonUtils.isValidEmailAddress(etEmail.getText().toString().trim())) {
            etEmail.setError(getString(R.string.please_enter_valid_email));
            return false;
        } else if (etPhone.getText().toString().isEmpty()) {
            etPhone.setError(getString(R.string.please_enter_phone));
            return false;
        } else if (etPhone.getText().toString().length() != 10) {
            etPhone.setError(getString(R.string.please_enter_valid_phone));
            return false;
        } else if (!etPassword.getText().toString().isEmpty()) {
            if (etConfirmPassword.getText().toString().isEmpty()) {
                etConfirmPassword.setError(getString(R.string.please_enter_password));
                return false;
            } else if (!etConfirmPassword.getText().toString().equals(etPassword.getText().toString())) {
                etConfirmPassword.setError(getString(R.string.password_not_matching));
                etPassword.setError(getString(R.string.password_not_matching));
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }


    }

}
