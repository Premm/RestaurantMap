package com.example.restaurantmap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Use a unique request code for each use case
    private static final int REQUEST_CODE_EXAMPLE = 1;
    ActivityResultLauncher<Intent> activityResultLauncher;

    List<Latlng> latlngs = new ArrayList<Latlng>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            assert data != null;
                            latlngs.add((Latlng)data.getSerializableExtra("PLACE"));
                        }
                    }
                });
    }

    public void addPlace(View view) {
        Intent intent = new Intent(this, NewPlace.class);
        activityResultLauncher.launch(intent);
    }

    public void showPlaces(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("PLACES", (Serializable) latlngs);
        startActivity(mapIntent);
    }
}