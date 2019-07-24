package com.example.loadmap4d;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vn.map4d.map4dsdk.annotations.MFMarker;
import vn.map4d.map4dsdk.annotations.MFMarkerOptions;
import vn.map4d.map4dsdk.maps.LatLng;
import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.maps.Map4D;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_CODE = 69;
    private Map4D map4D;

    Button viewMap3d;

    private  boolean defaultInfoWindow = true;
    private final List<MFMarker> markersList = new ArrayList<>();

    class CustomInfoWindowAdapter implements Map4D.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(MFMarker marker) {
            if (defaultInfoWindow) {
                return null;
            }
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(MFMarker marker) {
            return null;
        }

        private void render(MFMarker marker, View view) {
            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText(title);
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText(snippet);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MFSupportMapFragment mapFragment = (MFSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Map4D);
        mapFragment.getMapAsync(this);


        //getSupportActionBar().setTitle(R.string.myLocation);
        if (!isLocationPermissionEnable()) {
            requestLocationPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }

        viewMap3d = (Button) findViewById(R.id.map3D);
        viewMap3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewMap3D =new Intent(MainActivity.this, mode3d.class);
                startActivity(viewMap3D);
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

    //Marker
    private void addMarkersToMap() {
        //
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://api.myjson.com/bins/vi7vl", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for(int i = 0; i < response.length(); i++ ){
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            double lat = jsonObject.getDouble("Lat");
                            double lon = jsonObject.getDouble("Lon");
                            String nameMarker = jsonObject.getString("Name");
                            String busStopID = jsonObject.getString("BusStopId");
                            String tuyenDuong = jsonObject.getString("PlaceNameFromName");
                            MFMarker marker = map4D.addMarker(new MFMarkerOptions()
                                    .position(new LatLng(lat, lon ))
                                    .title("Tên trạm: " + nameMarker + "\n"+ " - ID: "+ busStopID)
                                    .snippet("Tuyến: "+tuyenDuong));
                            markersList.add(marker);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);
//        int numMarkersInRainbow = 5;
//        for (int i = 0; i < numMarkersInRainbow; i++) {
//            MFMarker marker = map4D.addMarker(new MFMarkerOptions()
//                    .position(new LatLng(10 + 0.8 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)), 106 - 0.8 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
//                    .title("Marker  " + i)
//                    .snippet(String.format("%f", 10 + 0.8 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)))
//                            + ", "
//                            + String.format("%f",106 - 0.8 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1)))));
//            markersList.add(marker);
//        }
//        View view = createMarkerView();
//        MFMarker markerView = map4D.addMarker(new MFMarkerOptions()
//                .position(new LatLng(13.0006, 106.784))
//                .title("Marker  13")
//                .snippet(13.0006f + ", " + 106.784f)
//                .iconView(view));
//        markersList.add(markerView);
    }
    //Marker



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
        this.map4D = map4D;
        addMarkersToMap();
        map4D.setInfoWindowAdapter(new CustomInfoWindowAdapter());
//        map4D.setOnMyLocationButtonClickListener(new Map4D.OnMyLocationButtonClickListener() {
//            @Override
//            public boolean onMyLocationButtonClick() {
//                Toast.makeText(getApplicationContext(), "My Location Button clicked", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

        map4D.setOnMyLocationClickListener(new Map4D.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(Location location) {
                Toast.makeText(getApplicationContext(), location.getLatitude()+"_"+location.getLongitude(), Toast.LENGTH_SHORT).show();
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
