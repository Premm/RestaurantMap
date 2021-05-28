package com.example.restaurantmap;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    List<Latlng> latlngs = new ArrayList<Latlng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent mapIntent = getIntent();
        latlngs = (List<Latlng>) mapIntent.getSerializableExtra("PLACES");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for(int  i = 0; i < latlngs.size(); ++i){
            LatLng temp = new LatLng(latlngs.get(i).getLat(), latlngs.get(i).getLng());
            mMap.addMarker(new MarkerOptions().position(temp).title(latlngs.get(i).getName()));
            if(i == 0){

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(temp, 5));
            }
        }

    }
}