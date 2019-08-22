package com.map4d.vbus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.map4d.map4dsdk.annotations.MFMarker;
import vn.map4d.map4dsdk.annotations.MFPolygonOptions;
import vn.map4d.map4dsdk.annotations.MFPolyline;
import vn.map4d.map4dsdk.annotations.MFPolylineOptions;
import vn.map4d.map4dsdk.maps.LatLng;
import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.maps.Map4D;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;

public class example1 extends AppCompatActivity implements OnMapReadyCallback {
    private MFPolyline polyline;
    private MFMarker marker;
    private MFPolygonOptions polygonOptions;
    private Map4D map4D;
    private List<LatLng> LatLngList = new ArrayList<>();
    private static final int REQUEST_LOCATION_CODE = 69;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example1);

        MFSupportMapFragment mapFragment = (MFSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2D);
        mapFragment.getMapAsync(this);

    }
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

    private void createpath(){
        LatLngList = decodePolyLine("i{`aBw{lsSGaCOiBEGIAIF?LFPJt@NxFL~ATpJ@tCXfQBhBC|Fi@jZQdL@rEJnCXdHd@`PLxBFbBHrAFNrD~EH\\\\rAtBbCnDbChDVf@B^Ep@A^@PPt@@l@Kj@oBtDoGnKgAtBmDdG_J|OcHbL{CzFwBtDuAhCsC|EkArB{@`ByBtDuAlCwA~AMBMDORCN?PHXLLRFR?JC\\\\RnC|ApBvAzDlC~FvDlP`KnFdDZz@Fh@AZyDjGk@r@y@`As@l@i@^sAp@s@R{DhAkKbDoHzBmDbAgH|BmEpAcDfAyUhHyM~DqCx@J\\\\HJh@n@dJbIxCnClJ~HtDpClDpCkDdE}LfOkAbBcAtAsEtFm@|@kAzAaItJ`IuJjA{Al@}@hC}CiC|Cm@|@iIbKwG`IqCpDoD~E}CrEcC|Ca@h@{GfI`LfJpE|DlZbWZLrBfBtIpHhHdF|CnBtFlCnAjA\\\\XfAf@jG`F`GtEnO|LvGdFzOdMpGfGtKbLl@r@b@x@bAhCj@lAd@p@rD~DrArAVVz@hAiBvAsBpB}Al@K@Nh@FLsDvCgBv@oInBuOdDeDx@{Bl@yBx@qAz@eAv@sDbDiBdB_IbHmCnBqCjBmPhJiJtFq@XiA`@iB`@cALoAFcSNqLBgCFeCE}@KiB[oC{@_FoByFoBgDyA_GmCSBEBENBTJRVv@RdAJhAIlKYnV?bGFlA");
    }

    //Polyline
    private void addPolyline(){
        polyline = map4D.addPolyline(new MFPolylineOptions().add(LatLngList.toArray(new LatLng[LatLngList.size()]))
                .color("#ff00")
                .width(8)
                .closed(false)
                .alpha(0.3f));

    }
    //decode
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
    @Override
    public void onMapReady(Map4D map4D) {
        createpath();

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
