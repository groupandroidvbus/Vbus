package com.map4d.vbus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.map4d.map4dsdk.camera.MFCameraUpdateFactory;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;
;
import vn.map4d.map4dsdk.maps.Map4D;

import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.annotations.MFBitmapDescriptorFactory;
import vn.map4d.map4dsdk.maps.LatLng;
import vn.map4d.map4dsdk.annotations.MFMarker;
import vn.map4d.map4dsdk.annotations.MFMarkerOptions;
import vn.map4d.map4dsdk.annotations.MFPolyline;
import vn.map4d.map4dsdk.annotations.MFPolylineOptions;
import com.map4d.vbus.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;
import vn.map4d.map4dsdk.maps.Map4D;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private Map4D map4D;
    private Button btnFindPath;
    private Button btn3dmode;
    private MFSupportMapFragment mapFragment;

    private List<MFMarker> originMarkers = new ArrayList<>();
    private List<MFMarker> destinationMarkers = new ArrayList<>();
    private List<MFPolyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String etOrigin = "Công viên 29/3, Thạc Gián, Thanh Khê, Đà Nẵng";
    private String etDestination = "Đường Trung Tâm - Khu công nghệ cao, Hoà Liên, Hoà Vang, Đà Nẵng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (MFSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        btn3dmode = (Button)findViewById(R.id.map3D);
//        btn3dmode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, mode3d.class);
//                startActivity(intent);
//            }
//        });

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {
        String origin = etOrigin;
        String destination = etDestination;
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (MFMarker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (MFMarker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (MFPolyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            map4D.moveCamera(MFCameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(map4D.addMarker(new MFMarkerOptions()
                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(map4D.addMarker(new MFMarkerOptions()
                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            MFPolylineOptions polylineOptions = new MFPolylineOptions().
                    color("#ff00")
                    .width(8)
                    .closed(false)
                    .alpha(0.3f);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(map4D.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onMapReady(Map4D map4D) {
        map4D = map4D;
        LatLng hcmus = new LatLng(16.066004,108.205557);
        map4D.moveCamera(MFCameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(map4D.addMarker(new MFMarkerOptions()
                .title("Điểm đầu")
                .position(hcmus)));

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
