package com.hotapps.easyplant.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.hotapps.easyplant.R;
import com.hotapps.easyplant.common.BottomNavigationViewHelper;
import com.hotapps.easyplant.common.PreManager;
import com.hotapps.easyplant.fragments.FragmentFeed;
import com.hotapps.easyplant.fragments.FragmentNearBy;
import com.hotapps.easyplant.fragments.ProfileFragment;
import com.hotapps.easyplant.fragments.SearchHistoryFragment;
import com.hotapps.easyplant.fragments.SnapFragment;
import com.hotapps.easyplant.model.LoginDetailsM;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int PERMISSION_ALL = 1;
    TextView tvTitle;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA

    };
    ImageView ivLogout;
    PreManager preManager;
    LoginDetailsM loginDetailsM;
    GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        enableGps();





    }

    public void  initViews(){
        ivLogout = (ImageView) findViewById(R.id.ivLogout);
        tvTitle =(TextView)findViewById(R.id.tvTitle);
        Intent i = getIntent();

        preManager = new PreManager(MainActivity.this);
        loginDetailsM = preManager.getLoginDetails();
        loadFragment(new FragmentFeed());
        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preManager.setLoginDetails(null);
                Toast.makeText(MainActivity.this, "Logout Successfully!!!!", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_feed) {
                    tvTitle.setText(getString(R.string.app_name));
                    loadFragment(new FragmentFeed());
                } else if (menuItem.getItemId() == R.id.action_explore) {
                    tvTitle.setText(getString(R.string.plant_shops));

                    loadFragment(new FragmentNearBy());
                } else if (menuItem.getItemId() == R.id.action_history) {
                    if (loginDetailsM != null) {
                        tvTitle.setText(getString(R.string.search_history));
                        loadFragment(new SearchHistoryFragment());
                    } else {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                } else if (menuItem.getItemId() == R.id.action_snap) {
                    if (loginDetailsM != null) {
                        tvTitle.setText(getString(R.string.snap_plant));
                        loadFragment(new SnapFragment());
                    } else {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                } else {
                    if (loginDetailsM != null) {
                        tvTitle.setText(getString(R.string.profile));
                        loadFragment(new ProfileFragment());
                    } else {
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                }
                return false;
            }
        });
    }

    public  void enableGps(){
        final LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
        }
        // Todo Location Already on  ... end

        if(!hasGPSDevice(this)){
            Toast.makeText(this,"Gps not Supported",Toast.LENGTH_SHORT).show();
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Log.e("plant_finder","Gps already enabled");
          //  Toast.makeText(this,"Gps not enabled",Toast.LENGTH_SHORT).show();
            enableLoc();
        }else{
            Log.e("plant_finder","Gps already enabled");
        //    Toast.makeText(this,"Gps already enabled",Toast.LENGTH_SHORT).show();
        }
    }



    public void loadFragment(Fragment fragment) {
        String fragmentTag = fragment.getClass().getSimpleName();
        //   Log.e(TAG, fragment.getClass().getSimpleName().toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean fragmentPopped = fragmentManager.popBackStackImmediate(fragmentTag, 0);
        if (!fragmentPopped) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_contaner, fragment, fragmentTag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commit();
        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_contaner);
        fragment.onActivityResult(requestCode, resultCode, data);



    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                              //  finish();
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }




}
