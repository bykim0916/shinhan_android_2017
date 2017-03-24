package com.shinhan.sassetmanager;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

public class PanoramaActivity extends AppCompatActivity
        implements OnStreetViewPanoramaReadyCallback {

    private static final LatLng SAN_FRAN = new LatLng(37.765927, -122.449972);
    StreetViewPanorama mSvp;
    StreetViewPanoramaView mSvpView;
    SupportStreetViewPanoramaFragment supportStreetViewPanoramaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);

        supportStreetViewPanoramaFragment = (SupportStreetViewPanoramaFragment)getSupportFragmentManager().findFragmentById(R.id.supportstreetviewpanorama);
        supportStreetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {

            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                mSvp = streetViewPanorama;

                mSvpView = new StreetViewPanoramaView(getApplicationContext(),
                        new StreetViewPanoramaOptions().position(SAN_FRAN));

                mSvpView.animate();

            }
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "GPS 연동 권한 필요합니다.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

/*
supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

        StreetViewPanoramaFragment.
        StreetViewPanoramaOptions.
        //StreetViewPanoramaFragment.newInstance(StreetViewPanoramaOptions options) {}

        //mSvp.setPosition(SAN_FRAN);

        mSvpView = new StreetViewPanoramaView(this,
                new StreetViewPanoramaOptions().position(SAN_FRAN));

        StreetViewPanoramaLocation location = mSvp.getLocation();
        if (location != null && location.links != null) {
            mSvp.setPosition(location.links[0].panoId);
        }
        */
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
        panorama.setPosition(new LatLng(-33.87365, 151.20689));
    }



}
