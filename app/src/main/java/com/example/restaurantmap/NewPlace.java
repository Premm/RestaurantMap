package com.example.restaurantmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NewPlace extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Double currentLatitude;
    Double currentLongitude;

    Double selectedLatitude;
    Double selectedLongitude;

    EditText placeName;

    AutocompleteSupportFragment autocompleteFragment;
    private final String TAG = "RUNNING";


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);
        // Initialize the SDK
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);


        placeName = findViewById(R.id.name_edit_text);
// Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                if(place.getLatLng() != null) {
                    selectedLatitude = place.getLatLng().latitude;
                    selectedLongitude = place.getLatLng().longitude;
                }
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void getCurrentLocation(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            selectedLatitude = currentLatitude;
            selectedLongitude = currentLongitude;
            autocompleteFragment.setText(currentLatitude + ", " + currentLatitude);
        }
    }

    public void showLocation(View view) {
        ArrayList<Latlng> latlngs = new ArrayList<Latlng>();
        latlngs.add(new Latlng(selectedLatitude, selectedLongitude,placeName.getText().toString()));

        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("PLACES", latlngs);
        startActivity(mapIntent);
    }

    public void savePlace(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("PLACE", new Latlng(selectedLatitude, selectedLongitude, placeName.getText().toString()));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}