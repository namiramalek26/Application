package com.hotapps.easyplant.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.CMapFragment;
import com.hotapps.easyplant.model.googlePlace.GooglePlacesM;
import com.hotapps.easyplant.model.googlePlace.Result;

import org.json.JSONObject;


public class FragmentNearBy extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final int LOCATION_PERMISSION_CODE = 102;
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    CMapFragment mapFragment;
    LatLng currentLatLong;
    boolean isFirstTime = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_near_by, container, false);
        initGoogleMap();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initGoogleMap() {
        mapFragment = (CMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mapFragment.setListener(new CMapFragment.OnTouchListener() {
            @Override
            public void onTouch() {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_CODE);
            }
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.connect();
        super.onStop();
    }

    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSION_CODE);
            }
            return;
        } else {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                currentLatLong = new LatLng(latitude, longitude);
                if (isFirstTime) {
                  //  getNearByPlaces(latitude + "," + longitude);
                }
                Log.e("location", location.getLatitude() + "");
                moveMap();
            }
        }

    }

    private void moveMap() {
        mMap.addMarker(new MarkerOptions()
                .position(currentLatLong)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("My Location"));


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLatLong);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        //  int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults != null && grantResults.length == 2) {
            switch (requestCode) {
                case LOCATION_PERMISSION_CODE: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        getCurrentLocation();
                    } else {
                        showRequiredPermissionDialog();
                    }
                    return;
                }


            }
        }

    }

    private void showRequiredPermissionDialog() {
        new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Dialog_Alert)
                .setTitle(R.string.location_allow)
                .setMessage(R.string.permission_denied_message)
                .setPositiveButton(R.string.location_allow, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            dialog.cancel();
                            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    LOCATION_PERMISSION_CODE);
                        }
                    }
                })
                .setNegativeButton(R.string.location_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showRequiredPermissionDialog();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_map)
                .show();
    }


    private void getNearByPlaces(String location) {
        Log.e("location", location);
        AndroidNetworking.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .addQueryParameter("location", location)
                .addQueryParameter("radius", "1500")
                .addQueryParameter("type", "shop")
                .addQueryParameter("keyword", "Flower shop")
                .addQueryParameter("key", getString(R.string.map_key))
                .setTag("nearBy")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        GooglePlacesM googlePlacesM = new GsonBuilder().create().fromJson(response.toString(), GooglePlacesM.class);
                        Log.e("response", new GsonBuilder().create().toJson(googlePlacesM));
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Result result : googlePlacesM.getResults()) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_leaf_marker))
                                    .title(result.getName()));
                            builder.include(new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng()));
                        }
                        LatLngBounds bounds = builder.build();
                        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
                        int padding = (int) (width * 0.10);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Error", anError.getMessage());

                    }
                });
    }

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=1500&type=shop&keyword=flower&key=AIzaSyB7JMYQCuR0OqDrg2MqbxhR-tFNqKa2tbM

}
