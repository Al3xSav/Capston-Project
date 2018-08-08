package com.alexsav.stayfit.ui.activities;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.alexsav.stayfit.R;
import com.alexsav.stayfit.asynctasks.GymAsyncTask;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchGymActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String JSON_RESULTS = "results";
    public static final String JSON_GEOMETRY = "geometry";
    public static final String JSON_LOCATION = "location";
    public static final String JSON_LAT = "lat";
    public static final String JSON_LNG = "lng";
    public static final String JSON_NAME = "name";
    private static final int REQUEST_LOCATION = 0;
    private static GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private float mZoomLevel;
    private LatLng mCameraTarget;

    public static void extractGymLocation(String json) {
        try {
            JSONArray gymJsonArray = new JSONObject(json).getJSONArray(JSON_RESULTS);
            for (int i = 0; i < gymJsonArray.length(); i++) {
                double latitude = gymJsonArray
                        .getJSONObject(i)
                        .getJSONObject(JSON_GEOMETRY)
                        .getJSONObject(JSON_LOCATION)
                        .getDouble(JSON_LAT);

                double longitude = gymJsonArray
                        .getJSONObject(i)
                        .getJSONObject(JSON_GEOMETRY)
                        .getJSONObject(JSON_LOCATION)
                        .getDouble(JSON_LNG);

                String gymName = gymJsonArray
                        .getJSONObject(i)
                        .getString(JSON_NAME);

                LatLng gymCoordinates = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(gymCoordinates)
                        .title(gymName)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.search_gym);
        supportMapFragment.getMapAsync(this);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        onMapReady(mMap);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    SearchGymActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocationUI();
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(getString(R.string.zoom_level_extra), mMap.getCameraPosition().zoom);
        outState.putParcelable
                (getString(R.string.camera_target_extra), mMap.getCameraPosition().target);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mZoomLevel = savedInstanceState.getFloat(getString(R.string.zoom_level_extra));
        mCameraTarget = savedInstanceState.getParcelable(getString(R.string.camera_target_extra));
    }

    public void updateLocationUI() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(
                                        location.getLatitude(),
                                        location.getLongitude());

                                if (mZoomLevel == 0.0f) {
                                    mZoomLevel = 12.0f;
                                }

                                if (mCameraTarget == null) {
                                    mCameraTarget = currentLocation;
                                }

                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLocation)
                                        .title(getString(R.string.current_location)));

                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mCameraTarget, mZoomLevel));
                                String currentString = currentLocation.toString();
                                currentString = currentString
                                        .substring(
                                                currentString.indexOf("(") + 1,
                                                currentString.indexOf(")"));

                                new GymAsyncTask().execute(currentString);
                            }
                        }
                    });
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

}
