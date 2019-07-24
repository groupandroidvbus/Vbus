package com.example.loadmap4d;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.maps.Map4D;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;

public class mode3d extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_CODE = 69;
    private Map4D map4D;

    private MFSupportMapFragment mapFragment;
    Button viewMap2D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode3d);

        mapFragment = (MFSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map3D);
        mapFragment.getMapAsync(this);
        getSupportActionBar().setTitle("Map 3D");

        viewMap2D = (Button) findViewById(R.id.map2D);
        viewMap2D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewMap2D = new Intent(mode3d.this, MainActivity.class);
                startActivity(viewMap2D);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    @Override
//    public void onMapReady(Map4D map4D) {
//        map4D.setMinZoomPreference(17.f);
//        map4D.enable3DMode(true);
//    }

    private void requestLocationPermission(String[] permission) {
        ActivityCompat.requestPermissions(this, permission, REQUEST_LOCATION_CODE);
    }

    boolean isLocationPermissionEnable() {
        boolean isLocationPermissionenabed = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isLocationPermissionenabed = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return isLocationPermissionenabed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_CODE: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    map4D.setMyLocationEnabled(true);
                    map4D.setOnMyLocationButtonClickListener(new Map4D.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            Toast.makeText(getApplicationContext(), "My Location button clicked", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                } else {
                    Toast.makeText(this, "Need Allow Location Permission to use Location feature", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    @Override
    public void onMapReady(Map4D map4D) {

        map4D.setMinZoomPreference(17.f);
        map4D.enable3DMode(true);
        this.map4D = map4D;
        map4D.setOnMyLocationButtonClickListener(new Map4D.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                Toast.makeText(getApplicationContext(), "My Location Button clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        map4D.setOnMyLocationClickListener(new Map4D.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(Location location) {
                Toast.makeText(getApplicationContext(), "My Location Icon clicked", Toast.LENGTH_SHORT).show();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map4D.setMyLocationEnabled(true);
    }
}
