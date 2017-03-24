package com.shinhan.sassetmanager;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class SunnySearchActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    GoogleMap googleMap;
    int curIndex = 0;
    boolean visibleFlag = false;
    String gImageId = "";
    double gImageLati = 0.0;
    double gImageLongi = 0.0;
    int gImageCount = 0;

    class MyMarker {
        String name;
        LatLng latLng;
        MyMarker(String name, LatLng latLng) {
            this.name = name;
            this.latLng = latLng;
        }
    }

    MyMarker[] markers = {

            new MyMarker("고양법원", new LatLng(37.652851, 126.774624)),
            new MyMarker("국립암센터", new LatLng(37.664287, 126.783786)),
            new MyMarker("마두역", new LatLng(37.650527, 126.778106)),
            new MyMarker("백마", new LatLng(37.653869, 126.789951)),
            new MyMarker("신한PWM일산센터", new LatLng(37.662325, 126.770711)),
            new MyMarker("일산강촌마을", new LatLng(37.652888, 126.784828)),
            new MyMarker("일산금융센터", new LatLng(37.662325, 126.770711)),
            new MyMarker("일산중앙", new LatLng(37.670826, 126.760470)),
            new MyMarker("일산호수공원", new LatLng(37.668508, 126.756497)),
            new MyMarker("나의위치", new LatLng(37.662325, 126.770711))
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunny_search);


        supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googlemap) {
                googleMap = googlemap;    //비동기방식으로 구글지도 객체 얻기

                //PolylineOptions rectOptions = new PolylineOptions();
                //rectOptions.color(Color.RED);

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
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18));
                            //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                            //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
                            ImageView imageView = (ImageView)findViewById(R.id.imageview);
                            if (visibleFlag && googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ) {
                                imageView.setVisibility(ImageView.VISIBLE);
                            } else {
                                imageView.setVisibility(ImageView.INVISIBLE);
                            }


                            return false;
                        }
                    });

                    //rectOptions.add(markers[i].latLng);
                }
                //Polyline polyline = googleMap.addPolyline(rectOptions);
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

        String temp = "";
        if (googleMap == null) {
            temp = "this is null";
        } else {
            temp = "this is not null";
        }
        Log.i("googleMap", "###################" + temp);

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
            Location location = new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(37.662300);
            location.setLongitude(126.770700);

            if (location != null) {
                TextView textView = (TextView) findViewById(R.id.location);
                textView.setText("내위치 : " + location.getLatitude() + " , 경도 : " + location.getLongitude());

                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
                if (googleMap != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 18));
                }

                Toast.makeText(SunnySearchActivity.this, "Last Known Location 위도:" + location.getLatitude() + " , 경도:" + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
            GPSListener gpsListener = new GPSListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);

            //현재위치와 가까운 이미지가 있고 해당 이미지를 한번도 클릭한 적 없으면 보여줌
            ImageView imageView = (ImageView)findViewById(R.id.imageview);
            Location curLocation = new Location(LocationManager.GPS_PROVIDER);
            curLocation.setLatitude(location.getLatitude());
            curLocation.setLongitude(location.getLongitude());

            SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(SunnySearchActivity.this);
            SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();

            Cursor cursor1 = database.rawQuery("select * from " + SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null);
            String[] imageId = new String[cursor1.getCount()];
            double[] imageLati = new double[cursor1.getCount()];
            double[] imageLongi = new double[cursor1.getCount()];
            for (int i = 0 ; i < cursor1.getCount() ; i++) {
                cursor1.moveToNext();
                imageId[i] = String.valueOf(cursor1.getInt(0));
                imageLati[i] = cursor1.getDouble(1);
                imageLongi[i] = cursor1.getDouble(2);

                Location imageLocation = new Location(LocationManager.GPS_PROVIDER);
                imageLocation.setLatitude(imageLati[i]);
                imageLocation.setLongitude(imageLongi[i]);

                double distance = curLocation.distanceTo(imageLocation);
                if (distance > -10 && distance < 10) {
                    gImageId = imageId[i];
                    gImageLati = imageLati[i];
                    gImageLongi = imageLongi[i];
                    gImageCount = 1;
                    break;
                }

                Log.i("distance", "################==>" + distance + "");
                Log.i("curLocation", "################==>" + curLocation.getLatitude() + ", " + curLocation.getLongitude());
                Log.i("imageLocation", "################==>" + imageLocation.getLatitude() + ", " + imageLocation.getLongitude());
            }
            if (gImageCount == 0) {
                gImageId = "";
                gImageLati = 0.0;
                gImageLongi = 0.0;
            }

            String memberId = "bykim0916";
            String sqlSelect = "" +
                    "select * " +
                    "from event_his a " +
                    "where a.member_id = '" + memberId + "' " +
                    "  and a.image_id = " + gImageId + " ";
            Cursor cursor2 = database.rawQuery(sqlSelect, null);

            Log.i("count2" , "##############"+cursor2.getCount()+"");

            if (cursor2.getCount() == 0) {
                visibleFlag = true;
                //imageView.setVisibility(ImageView.VISIBLE);
            } else {
                visibleFlag = false;
                //imageView.setVisibility(ImageView.INVISIBLE);
            }
/*
            //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
            for (int j = 0 ; j < markers.length ; j++) {
                Location bankLocation = new Location(LocationManager.GPS_PROVIDER);
                bankLocation.setLatitude(markers[j].latLng.latitude);
                bankLocation.setLongitude(markers[j].latLng.longitude);
                double distance = curLocation.distanceTo(bankLocation);
                if (distance > -10 && distance < 10) {
                    visibleFlag = true;
                    //imageView.setVisibility(ImageView.VISIBLE);
                } else {
                    visibleFlag = false;
                    //imageView.setVisibility(ImageView.INVISIBLE);
                }

                //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
                if (visibleFlag && googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ) {
                    imageView.setVisibility(ImageView.VISIBLE);
                } else {
                    imageView.setVisibility(ImageView.INVISIBLE);
                }

                Log.i("distance", "################==>" + distance + "");
                Log.i("curLocation", "################==>" + curLocation.getLatitude() + ", " + curLocation.getLongitude());
                Log.i("bankLocation", "################==>" + bankLocation.getLatitude() + ", " + bankLocation.getLongitude());
            */
        }
    }
    /*
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

                Toast.makeText(SunnySearchActivity.this, "Last Known Location 위도:" + location.getLatitude() + " , 경도:" + location.getLongitude(), Toast.LENGTH_LONG).show();
            }
            GPSListener gpsListener = new GPSListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, gpsListener);
        }

    }
*/
    private class GPSListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();    //위도
            double longitude = location.getLongitude();  //경도
            TextView textView = (TextView)findViewById(R.id.location);
            textView.setText("내위치 : " + latitude + " , " + longitude);
            Toast.makeText(SunnySearchActivity.this, "위도:" + latitude + " , 경도:" + longitude, Toast.LENGTH_LONG).show();

            LatLng curPoint = new LatLng(latitude, longitude);
            if (googleMap != null) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
            }

            //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
            ImageView imageView = (ImageView)findViewById(R.id.imageview);
            Location curLocation = new Location(LocationManager.GPS_PROVIDER);
            curLocation.setLatitude(latitude);
            curLocation.setLongitude(longitude);
            for (int j = 0 ; j < markers.length ; j++) {
                Location bankLocation = new Location(LocationManager.GPS_PROVIDER);
                bankLocation.setLatitude(markers[j].latLng.latitude);
                bankLocation.setLongitude(markers[j].latLng.longitude);
                double distance = curLocation.distanceTo(bankLocation);
                if (distance > -10 && distance < 10) {
                    imageView.setVisibility(ImageView.VISIBLE);
                } else {
                    imageView.setVisibility(ImageView.INVISIBLE);
                }

                Log.i("distance", "################==>" + distance + "");
                Log.i("curLocation", "################==>" + curLocation.getLatitude() + ", " + curLocation.getLongitude());
                Log.i("bankLocation", "################==>" + bankLocation.getLatitude() + ", " + bankLocation.getLongitude());
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

            //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
            ImageView imageView = (ImageView)findViewById(R.id.imageview);
            if (visibleFlag && googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ) {
                imageView.setVisibility(ImageView.VISIBLE);
            } else {
                imageView.setVisibility(ImageView.INVISIBLE);
            }
        }
    }

    public void onTourButtonClicked(View v) {

        if (curIndex > markers.length - 1) {
            curIndex = 0;
        }

        if (googleMap != null) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers[curIndex++].latLng, 17));

            //현재위치와 영업점 사이의 거리가 가까우면 이미지를 보여줌
            ImageView imageView = (ImageView)findViewById(R.id.imageview);
            if (visibleFlag && googleMap.getMapType() == GoogleMap.MAP_TYPE_SATELLITE ) {
                imageView.setVisibility(ImageView.VISIBLE);
            } else {
                imageView.setVisibility(ImageView.INVISIBLE);
            }
        }
    }

    public void onImageViewClicked(View v) {
        Toast.makeText(SunnySearchActivity.this, "축하드립니다. 고객님의 써니점수는 7 점 입니다.", Toast.LENGTH_LONG).show();

        //이미지 클릭시 사라지게 함
        ImageView imageView = (ImageView)findViewById(R.id.imageview);
        visibleFlag = false;
        imageView.setVisibility(ImageView.INVISIBLE);
    }
}
