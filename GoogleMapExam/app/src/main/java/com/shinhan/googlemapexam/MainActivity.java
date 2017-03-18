package com.shinhan.googlemapexam;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    int curIndex = 0;

    class MyMarker {
        String name;
        LatLng latLng;
        MyMarker(String name, LatLng latLng) {
            this.name = name;
            this.latLng = latLng;
        }
    }

    MyMarker[] markers = {
        new MyMarker("집", new LatLng(37.271766, 127.042923)),
        new MyMarker("버스정류장", new LatLng(37.273477, 127.050943)),
        new MyMarker("신한은행본점", new LatLng(37.560952, 126.974717)),
        new MyMarker("일산동구청", new LatLng(37.657793, 126.774333)),
        new MyMarker("신한은행일산센터", new LatLng(37.662325, 126.770711)),
        new MyMarker("일산경찰서", new LatLng(37.664113, 126.769472)),
        new MyMarker("강남역", new LatLng(37.496003, 127.028241)),
        new MyMarker("KT", new LatLng(37.273254, 127.051038))
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googlemap) {
                googleMap = googlemap;    //비동기방식으로 구글지도 객체 얻기

                PolylineOptions rectOptions = new PolylineOptions();
                rectOptions.color(Color.RED);

                for (int i = 0 ; i < markers.length ; i++) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(markers[i].latLng);
                    marker.title(markers[i].name);
                    googleMap.addMarker(marker);

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            for (int j = 0 ; j < markers.length ; j++) {
                                if (marker.getTitle().equals(markers[j].name)) {
                                    if (j + 1 < markers.length) {
                                        curIndex = j + 1;
                                    } else {
                                        curIndex = 0;
                                    }
                                    break;
                                }
                            }

                            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));

                            return false;
                        }
                    });

                    rectOptions.add(markers[i].latLng);
                }
                Polyline polyline = googleMap.addPolyline(rectOptions);
            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "GPS 연동 권한 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "GPS 권한 승인!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "GPS 권한 거부!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startLocationService(View view) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                TextView textView = (TextView) findViewById(R.id.location);
                textView.setText("내위치 : " + location.getLatitude() + " , 경도 : " + location.getLongitude());

                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
                }

                Toast.makeText(MainActivity.this, "Last Known Location 위도:" + location.getLatitude() + " , 경도:" + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
            GPSListener gpsListener = new GPSListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);
        }


    }

    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();    //위도
            double longitude = location.getLongitude();  //경도
            TextView textView = (TextView)findViewById(R.id.location);
            textView.setText("내위치 : " + latitude + " , " + longitude);
            Toast.makeText(MainActivity.this, "위도:" + latitude + " , 경도:" + longitude, Toast.LENGTH_LONG).show();

            LatLng curPoint = new LatLng(latitude, longitude);
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }

    public void onWorldMapButtonClicked(View v) {
        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(1));
        }
    }

    public void onTourButtonClicked(View v) {

        if (curIndex > markers.length - 1) {
            curIndex = 0;
        }

        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers[curIndex++].latLng, 17));
        }
    }

}


