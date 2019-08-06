package com.map4d.vbus;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
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
import com.map4d.vbus.R;
import com.map4d.vbus.mode3d;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.map4d.map4dsdk.annotations.MFBitmapDescriptorFactory;
import vn.map4d.map4dsdk.annotations.MFCircle;
import vn.map4d.map4dsdk.annotations.MFCircleOptions;
import vn.map4d.map4dsdk.annotations.MFMarker;
import vn.map4d.map4dsdk.annotations.MFMarkerOptions;
import vn.map4d.map4dsdk.annotations.MFPolyline;
import vn.map4d.map4dsdk.annotations.MFPolylineOptions;
import vn.map4d.map4dsdk.maps.LatLng;
import vn.map4d.map4dsdk.maps.MFSupportMapFragment;
import vn.map4d.map4dsdk.maps.Map4D;
import vn.map4d.map4dsdk.maps.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_CODE = 69;
    private Map4D map4D;
    DecimalFormat decimalFormat = new DecimalFormat("0.000");
    Button viewMap3d;
    private  boolean defaultInfoWindow = true;
    private final List<MFMarker> markersList = new ArrayList<>();
    private List<LatLng> LatLngList = new ArrayList<>();
    private List<LatLng> LatLngNgaRe = new ArrayList<>();
    private List<LatLng> LatLngNgaRe2 = new ArrayList<>();
    private MFPolyline polyline;
    private MFCircle circle;
    private double[] khoangcach = new double[100];
    TextToSpeech textToSpeech;
    private LocationManager locationManager;
    private LocationListener listener;



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

        //Location
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

    //polyline: giả lập tuyến bus
    private void creatPath(){
        LatLngList.add(new LatLng(16.056027725183,108.17246204884));//1
        LatLngList.add(new LatLng(16.059193410072,108.17493374949));//2
        LatLngList.add(new LatLng(16.062642303564,108.17986358281));//3
        LatLngList.add(new LatLng(16.065368915846,108.18442832196));//4
        LatLngList.add(new LatLng(16.065724604956,108.18888078893));//5
        LatLngList.add(new LatLng(16.069164,108.191942));//6
        LatLngList.add(new LatLng(16.072415810515,108.19254278008));//7
        LatLngList.add(new LatLng(16.072333334711,108.19755314653));//8
        LatLngList.add(new LatLng(16.073353970365,108.20198415582));//9
        LatLngList.add(new LatLng(16.078842,108.211714));//10
        LatLngList.add(new LatLng(16.077860,108.211913));//11
        LatLngList.add(new LatLng(16.074977,108.212494));//12
        LatLngList.add(new LatLng(16.072783,108.212912));//13
        LatLngList.add(new LatLng(16.070504180136,108.2149773683));//14
        LatLngList.add(new LatLng(16.068523946288,108.21556686227));//15
        LatLngList.add(new LatLng(16.06803270253,108.21794118251));//16
        LatLngList.add(new LatLng(16.068598,108.221833));//17
        LatLngList.add(new LatLng(16.067115214435,108.2237093266));//18
        LatLngList.add(new LatLng(16.061998549605,108.2231957908));//19
        LatLngList.add(new LatLng(16.052831,108.218032));//20
        LatLngList.add(new LatLng(16.055300820364,108.22013241311));//21
        LatLngList.add(new LatLng(16.052918189291,108.22040663538));//22
        LatLngList.add(new LatLng(16.049205,108.221172));//23
        LatLngList.add(new LatLng(16.052585,108.237009));//24
        LatLngList.add(new LatLng(16.04774,108.238672));//25
        LatLngList.add(new LatLng(16.045417,108.239301));//26
        LatLngList.add(new LatLng(16.042743,108.24068));//27
        LatLngList.add(new LatLng(16.039723,108.242166));//28
        LatLngList.add(new LatLng(16.036606203452,108.24368303974));//29
        LatLngList.add(new LatLng(16.032571988877,108.2458822566));//30
        LatLngList.add(new LatLng(16.02969313753,108.24725947045));//31
        LatLngList.add(new LatLng(16.025458031964,108.24937736947));//32
        LatLngList.add(new LatLng(16.017801012594,108.25328156617));//33
        LatLngList.add(new LatLng(16.010588485555,108.25680757123));//34
        LatLngList.add(new LatLng(16.006919331911,108.258677822));//35
        LatLngList.add(new LatLng(16.002392,108.259765));//36
        LatLngList.add(new LatLng(16.099081, 108.223968));//marker-gia lap
        LatLngList.add(new LatLng(16.095989,108.224359));//marker - gia lap
        LatLngList.add(new LatLng(16.093157, 108.225284));//marker - gia lap
        LatLngList.add(new LatLng(16.090473,108.226853));//marker - gia lap
        LatLngList.add(new LatLng(16.088133,108.229004));//marker - gia lap

        //polylineFake
        LatLngNgaRe.add(new LatLng(16.056027725183,108.17246204884));//1
        LatLngNgaRe.add(new LatLng(16.057422,108.172358));
        LatLngNgaRe.add(new LatLng(16.059193410072,108.17493374949));//2
        LatLngNgaRe.add(new LatLng(16.062642303564,108.17986358281));//3
        LatLngNgaRe.add(new LatLng(16.065368915846,108.18442832196));//4
        LatLngNgaRe.add(new LatLng(16.065724604956,108.18888078893));//5
        LatLngNgaRe.add(new LatLng(16.065808,108.192050));
        LatLngNgaRe.add(new LatLng(16.069164,108.191942));//6
        LatLngNgaRe.add(new LatLng(16.072442, 108.191927));
        LatLngNgaRe.add(new LatLng(16.072415810515,108.19254278008));//7
        LatLngNgaRe.add(new LatLng(16.072333334711,108.19755314653));//8
        LatLngNgaRe.add(new LatLng(16.073353970365,108.20198415582));//9
        LatLngNgaRe.add(new LatLng(16.080279,108.211070));
        LatLngNgaRe.add(new LatLng(16.078842,108.211714));//10
        LatLngNgaRe.add(new LatLng(16.077860,108.211913));//11
        LatLngNgaRe.add(new LatLng(16.074977,108.212494));//12
        LatLngNgaRe.add(new LatLng(16.072783,108.212912));//13
        LatLngNgaRe.add(new LatLng(16.070238,108.213432));
        LatLngNgaRe.add(new LatLng(16.070504180136,108.2149773683));//14
        LatLngNgaRe.add(new LatLng(16.070580, 108.215064));
        LatLngNgaRe.add(new LatLng(16.068523946288,108.21556686227));//15
        LatLngNgaRe.add(new LatLng(16.067622, 108.215856));
        LatLngNgaRe.add(new LatLng(16.06803270253,108.21794118251));//16
        LatLngNgaRe.add(new LatLng(16.068598,108.221833));//17
        LatLngNgaRe.add(new LatLng(16.068587, 108.223923));
        LatLngNgaRe.add(new LatLng(16.067115214435,108.2237093266));//18
        LatLngNgaRe.add(new LatLng(16.061998549605,108.2231957908));//19
        LatLngNgaRe.add(new LatLng(16.052831,108.218032));//20
        LatLngNgaRe.add(new LatLng(16.055300820364,108.22013241311));//21
        LatLngNgaRe.add(new LatLng(16.060591, 108.223087));
        LatLngNgaRe.add(new LatLng(16.060612, 108.223087));
        LatLngNgaRe.add(new LatLng(16.060499, 108.222989));
        LatLngNgaRe.add(new LatLng(16.059122, 108.221381));
        LatLngNgaRe.add(new LatLng(16.056803, 108.220227));
        LatLngNgaRe.add(new LatLng(16.054714, 108.220129));
        LatLngNgaRe.add(new LatLng(16.052918189291,108.22040663538));//22
        LatLngNgaRe.add(new LatLng(16.049409, 108.220790));
        LatLngNgaRe.add(new LatLng(16.049302, 108.220767));
        LatLngNgaRe.add(new LatLng(16.049226, 108.220846));
        LatLngNgaRe.add(new LatLng(16.049299, 108.222133));
        LatLngNgaRe.add(new LatLng(16.049226, 108.222383));
        LatLngNgaRe.add(new LatLng(16.049408, 108.222661));
        LatLngNgaRe.add(new LatLng(16.049205,108.221172));//23
        LatLngNgaRe.add(new LatLng(16.050980, 108.234740));
        LatLngNgaRe.add(new LatLng( 16.051358, 108.235455));
        LatLngNgaRe.add(new LatLng(16.053055, 108.236879));
        LatLngNgaRe.add(new LatLng(16.052585,108.237009));//24
        LatLngNgaRe.add(new LatLng(16.047690, 108.238432));//25
        LatLngNgaRe.add(new LatLng(16.045904, 108.239050));
        LatLngNgaRe.add(new LatLng(16.045417,108.239301));//26
        LatLngNgaRe.add(new LatLng(16.042743,108.24068));//27
        LatLngNgaRe.add(new LatLng(16.039723,108.242166));//28
        LatLngNgaRe.add(new LatLng(16.033164, 108.245463));
        LatLngNgaRe.add(new LatLng(16.036606203452,108.24368303974));//29
        LatLngNgaRe.add(new LatLng(16.031776, 108.246293));
        LatLngNgaRe.add(new LatLng(16.032571988877,108.2458822566));//30
        LatLngNgaRe.add(new LatLng(16.02969313753,108.24725947045));//31
        LatLngNgaRe.add(new LatLng(16.023497, 108.250361));
        LatLngNgaRe.add(new LatLng(16.025458031964,108.24937736947));//32
        LatLngNgaRe.add(new LatLng(16.017801012594,108.25328156617));//33
        LatLngNgaRe.add(new LatLng(16.009702, 108.257294));
        LatLngNgaRe.add(new LatLng(16.010588485555,108.25680757123));//34
        LatLngNgaRe.add(new LatLng(16.002524, 108.259540));
        LatLngNgaRe.add(new LatLng(16.006919331911,108.258677822));//35
        LatLngNgaRe.add(new LatLng(16.002392,108.259765));//36

        LatLngNgaRe2.add(new LatLng(16.099081, 108.223968));//marker
        LatLngNgaRe2.add(new LatLng(16.095989,108.224359));//marker
        LatLngNgaRe2.add(new LatLng(16.093157, 108.225284));//marker
        LatLngNgaRe2.add(new LatLng(16.090473,108.226853));//marker
        LatLngNgaRe2.add(new LatLng(16.088133,108.229004));//marker
    }
    //Marker
    private void addMarkersToMap() {
//        int i=1;
//        for (LatLng latLng : LatLngList) {
//            MFMarker marker = map4D.addMarker(new MFMarkerOptions()
//                    .position(latLng)
//                    .title("Trạm: " + i++)
//                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location))
//                    .snippet(latLng.getLatitude() + ", " + latLng.getLongitude())
//            );
//            markersList.add(marker);
//        }
        //Marker
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://vbusapp.000webhostapp.com/", new Response.Listener<JSONArray>() {
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
                                    .icon(MFBitmapDescriptorFactory.fromResource(R.drawable.location))
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
    }
    //Marker

    //Polyline
    private void addPolyline(){
        polyline = map4D.addPolyline(new MFPolylineOptions().add(LatLngNgaRe.toArray(new LatLng[LatLngNgaRe.size()]))
                .color("#ff00")
                .width(8)
                .closed(false)
                .alpha(0.3f));

    }
    private void addPolyline2(){
        polyline = map4D.addPolyline(new MFPolylineOptions().add(LatLngNgaRe2.toArray(new LatLng[LatLngNgaRe2.size()]))
                .color("#ff00")
                .width(8)
                .closed(false)
                .alpha(0.3f));

    }
    //Polyline

    //Circle
    private void addCircleToMap() {
        if (map4D != null) {
            for(LatLng latlng: LatLngList)
                circle = map4D.addCircle(new MFCircleOptions()
                        .center(latlng)
                        .radius(100)
                        .fillColor("#00ff00")
                        .fillAlpha(0.3f));
        }
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
    public void onMapReady(final Map4D map4D) {
        this.map4D = map4D;
        creatPath();
        addMarkersToMap();
        addPolyline();
        addPolyline2();
        addCircleToMap();
        map4D.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        final LatLng target = map4D.getCameraPosition().getTarget();


        //auto load my location
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                CountDownTimer countDownTimer = new CountDownTimer(86400000,10000) {
                    @Override
                    public void onTick(long l) {

                        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("https://vbusapp.000webhostapp.com/", new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (response != null) {
                                    try {
                                        for(int i = 0; i<response.length(); i++) {
                                            JSONObject jsonObject = response.getJSONObject(i);
                                            double lat2 = jsonObject.getDouble("Lat");
                                            double lon2 = jsonObject.getDouble("Lon");
                                            final String nameMarker = jsonObject.getString("Name");
                                            String busStopID = jsonObject.getString("BusStopId");
                                            String tuyenDuong = jsonObject.getString("PlaceNameFromName");
                                            int id = jsonObject.getInt("id");

                                            double lat1 = location.getLatitude();
                                            double lon1 = location.getLongitude();
                                            int R = 6371;
                                            double P1 = Math.toRadians(lat1);
                                            double N1 = Math.toRadians(lon1);
                                            double P2 = Math.toRadians(lat2);
                                            double N2 = Math.toRadians(lon2);


                                            double a = Math.cos(P1) *Math.cos(P2) * Math.sin((N2 - N1) / 2) * Math.sin((N2 - N1) / 2);
                                            double b = (Math.sin((P2 - P1) / 2)) * (Math.sin((P2 - P1) / 2));
                                            double c = Math.sqrt(a + b);
                                            double d = 2*R*Math.asin(c);


                                            double min = 4;
                                            if(0.03 < d && d < min){
                                                DecimalFormat precision = new DecimalFormat("0.000");//lấy 2 số thập phân
                                                final String d2 = precision.format(d);//chuyển kq về đạng chuỗi
                                                final String name = nameMarker;
                                                new CountDownTimer(86400000, 10000) {
                                                    public void onTick(long millisUntilFinished) {
                                                        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                                                            @Override
                                                            public void onInit(int status) {
                                                                if (status != TextToSpeech.ERROR) {
                                                                    textToSpeech.setLanguage(new Locale("vi"));
                                                                    textToSpeech.speak("quý khách chú ý ,Sắp đến " +name, TextToSpeech.QUEUE_FLUSH, null);
                                                                }
                                                            }
                                                        });                                                    }

                                                    public void onFinish() {
                                                        Toast.makeText(getApplicationContext(), "Lỗi rồi nhé", Toast.LENGTH_SHORT).show();
                                                    }
                                                }.start();

                                                Toast.makeText(getApplicationContext(), "Khoảng cách đến " + name + " là: " +d2 + " km", Toast.LENGTH_SHORT).show();
                                            } else if (0 < d && d ==0.03){
                                                DecimalFormat precision = new DecimalFormat("0.000");//lấy 2 số thập phân
                                                final String d2 = precision.format(d);//chuyển kq về đạng chuỗi
                                                final String name = nameMarker;
                                                textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                                                    @Override
                                                    public void onInit(int status) {
                                                        if (status != TextToSpeech.ERROR) {
                                                            textToSpeech.setLanguage(new Locale("vi"));
                                                            textToSpeech.speak("quý khách chú ý ,đã đến  " + name, TextToSpeech.QUEUE_FLUSH, null);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        requestQueue.add(jsonArrayRequest);
                    }

                    @Override
                    public void onFinish() {

                    }
                };
                countDownTimer.start();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

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
        locationManager.requestLocationUpdates("gps", 4000, 0, listener);
        configure_button();


        //auto load my location

        map4D.setOnMyLocationClickListener(new Map4D.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(final Location location) {
                Toast.makeText(getApplicationContext(), location.getLatitude()+"_"+location.getLongitude(), Toast.LENGTH_SHORT).show();

            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        map4D.setMyLocationEnabled(true);
    }
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},10);
            }
            return;
        }
    }
}