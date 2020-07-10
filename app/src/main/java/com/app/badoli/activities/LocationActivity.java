package com.app.badoli.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.app.badoli.R;
import com.app.badoli.config.AppUtils;
import com.app.badoli.config.Constant;
import com.app.badoli.databinding.ActivityLocationBinding;
import com.app.badoli.viewModels.AuthViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 123;
    private ActivityLocationBinding binding;

    private Geocoder mGeocoder;
    private GoogleMap googleMap;
    //private LatLng sourceLatLong;
    String current_address = "";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;

    String lattitude="",longitude="",address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }
    public void showLoading(String message){
        AppUtils.hideKeyboardFrom(LocationActivity.this);
        binding.layoutProgress.txtMessage.setText(message);
        binding.layoutProgress.lnProgress.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void dismissLoading(){
        binding.layoutProgress.lnProgress.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    private void init() {
        // type=getIntent().getStringExtra(Constant.EDIT_ADDRESS);
        checkAndRequestPermissions();
        binding.imgBack.setOnClickListener(this);
        binding.btnSelect.setOnClickListener(this);
        mGeocoder = new Geocoder(LocationActivity.this);
        binding.rlLocation.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
            Intent intent = new Autocomplete
                    .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(LocationActivity.this);
            startActivityForResult(intent, 1);
        });



        String apiKey = "AIzaSyBZ3O59KED5Kl--znKyOHMuhkdLvMJwc6Y";
        if (!Places.isInitialized()) {
            Places.initialize(LocationActivity.this, apiKey);
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
            supportMapFragment.onLowMemory();
        }

        if (AppUtils.isGooglePlayServicesAvailable(LocationActivity.this)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationActivity.this);
        }
        binding.txtCurrentLocation.setOnClickListener(view -> {
            if (checkAndRequestPermissions()) {
                if (!AppUtils.isLocationEnabled(LocationActivity.this)) {
                    AppUtils.showAlert(getString(R.string.gps_on_msg), LocationActivity.this);
                    return;
                }
                try {
                    if (googleMap != null) {
                        showLoading(getResources().getString(R.string.loading));
                        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LocationActivity.this, R.raw.style_json));
                        if (AppUtils.isLocationEnabled(LocationActivity.this)) {
                            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            googleMap.setMyLocationEnabled(true);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                            startCurrentLocationUpdates();
                        }
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e("Map:Style", "Can't find style. Error:");
                }
            }/*else {
                AppUtils.openPopup(LocationActivity.this,R.style.Dialod_UpDown,"error",getResources().getString(R.string.plz_enable_location));
            }*/

        });
    }

    private boolean checkAndRequestPermissions() {
        int ACCESS_FINE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ACCESS_FINE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        Log.d("msg", "Permission callback called-------");
        // Initialize the map with both permissions
        // Fill with actual results from user
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(LocationActivity.this, "Permission denied by user", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for both permissions

                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.e("msg", "All Permissions granted");
                    checkGPS();
                } else {
                    Log.e("msg", "Some permissions are not granted ask again ");
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                        showDialogOK(getResources().getString(R.string.permission_req),
                                (dialog, which) -> {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            checkAndRequestPermissions();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                           // enableLocation();
                                            // proceed with logic by disabling the related features or quit the app.
                                            break;
                                    }
                                });
                    }
                    //permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
                    else {
                        Toast.makeText(this, getResources().getString(R.string.enable_permission), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.okay), okListener)
                .setNegativeButton(getResources().getString(R.string.cancel), okListener)
                .create()
                .show();
    }
    private void checkGPS() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager != null) {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                enableLocation();
            } else {
               startCurrentLocationUpdates();
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void enableLocation() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.gps_disable_on))
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) ->
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) ->
                        dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }



    @Override
    public void onClick(View v) {
        if (v==binding.imgBack){
            finish();
        }
        if (v==binding.btnSelect) {
            showLoading(getResources().getString(R.string.loading));
            new Handler().postDelayed(() -> {
                dismissLoading();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constant.LATTITUDE,lattitude);
                resultIntent.putExtra(Constant.LONGITUDE,longitude);
                resultIntent.putExtra(Constant.ADDRESS_USER,address);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            },500);

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            Location currentLocation = locationResult.getLastLocation();
            setSelectedLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    };
    private GoogleMap.OnCameraIdleListener onIdleListener=new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
            LatLng midLatLng = googleMap.getCameraPosition().target;
            Log.e(TAG,"LATLNG--->"+midLatLng);
           showLoading(getResources().getString(R.string.loading));
            new Handler().postDelayed(() -> {
                if (midLatLng!=null&&midLatLng.latitude!=0.0){
                    try {
                        getPlaceInfo(midLatLng.latitude,midLatLng.longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            },500);
        }
    };

    @Override
    public void onMapReady(GoogleMap mapMap) {
        googleMap = mapMap;
        try {
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(LocationActivity.this, R.raw.style_json));
            if (AppUtils.isLocationEnabled(LocationActivity.this)) {
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                startCurrentLocationUpdates();
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map:Style", "Can't find style. Error:");
        }
        googleMap.setOnCameraIdleListener(onIdleListener);
    }

    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1500);
        locationRequest.setSmallestDisplacement(10);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LocationActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private void setSelectedLocation(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latLng.latitude,latLng.longitude), 16.0f));
        lattitude= String.valueOf(latLng.latitude);
        longitude= String.valueOf(latLng.longitude);

        String strSourceAddress = AppUtils.getCompleteAddressString(LocationActivity.this, latLng.latitude, latLng.longitude);
        if (!strSourceAddress.isEmpty()) {
            current_address = strSourceAddress;
        } else {
            current_address = AppUtils.getCompleteAddressString(LocationActivity.this, latLng.latitude, latLng.longitude);
        }
        address=current_address;
        binding.locationMarkertext.setText(current_address);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.isGooglePlayServicesAvailable(LocationActivity.this)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(LocationActivity.this);
            // startCurrentLocationUpdates();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.e(TAG, "Place: " + place.getAddress() + place.getPhoneNumber());

                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    Log.e(TAG, "PLACES--->" + place.getName() + ",\n" +
                            place.getAddress() + "\n" + place.getPhoneNumber() + "\nLat=" + latLng.latitude
                            + ",lng=" + latLng.longitude);
                    // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourceLatLong, 14.0f));
                   showLoading(getResources().getString(R.string.loading));
                    setSelectedLocation(latLng);
                    fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    try {
                        getPlaceInfo(latLng.latitude, latLng.longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "PLACES--->" + place.getName() + ",\n" +
                            place.getAddress() + "\n" + place.getPhoneNumber());
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                // TODO: Handle the error.
                Log.e(TAG, "--->" + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.e(TAG, "--->CANCELED");
            }
        }
    }

    private void getPlaceInfo(double lat, double lon) throws IOException {
        try {
           dismissLoading();
        }catch (Exception e){
            e.printStackTrace();
        }
        lattitude= String.valueOf(lat);
        longitude= String.valueOf(lon);
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0&&addresses.get(0).getAddressLine(0)!=null){

            String addressLine1=addresses.get(0).getAddressLine(0);
            Log.d("ADDRESS LINE 1", "getPlaceInfo: "+addressLine1);
            address=addressLine1;
            binding.locationMarkertext.setText(addressLine1);
        }
    }
}