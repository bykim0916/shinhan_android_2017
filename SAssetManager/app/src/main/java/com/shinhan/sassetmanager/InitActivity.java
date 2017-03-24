package com.shinhan.sassetmanager;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;

public class InitActivity extends AppCompatActivity {

    Toolbar toolbar;
    FundListFragment fundListFragment;
    MyAssetListFragment myAssetListFragment;
    EventListFragment eventListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        fundListFragment = new FundListFragment();
        myAssetListFragment = new MyAssetListFragment();
        eventListFragment = new EventListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fundListFragment).commit();

        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("펀드관리"));
        tabs.addTab(tabs.newTab().setText("자산관리"));
        tabs.addTab(tabs.newTab().setText("이벤트관리"));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("InitActivity", "선택된 탭 : " + position);

                Fragment selected = null;
                if (position == 0) {
                    selected = fundListFragment;
                } else if (position == 1) {
                    selected = myAssetListFragment;
                }
                else if (position == 2) {

                    selected = eventListFragment;
                    Intent intent = new Intent(InitActivity.this, SunnySearchActivity.class);
                    startActivity(intent);

                    //Uri gmmIntentUri = Uri.parse("google.streetview:cbll=37.7749,-122.4194");
                    //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    //mapIntent.setPackage("com.google.android.apps.maps");
                    //startActivity(mapIntent);


   /*

   MapView mapView = new MapView(InitActivity.this);
                    mapView.setDaumMapApiKey("API_KEY");
                    ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
                    mapViewContainer.addView(mapView);

                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("daummaps://roadView?p=37.537229,127.005515"));
                    startActivity(myIntent);



                    // Displays an image of the Swiss Alps
                    //Uri gmmIntentUri = Uri.parse("google.streetview:cbll=37.6623,-126.7707");
                    //Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");
                    Uri gmmIntentUri = Uri.parse("google.streetview:cbll=37.7749,-122.4194");

                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);


                    Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }




                    // Create a Uri from an intent string. Use the result to create an Intent.
                    Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
// Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps");

// Attempt to start an activity that can handle the Intent
                    startActivity(mapIntent);
*/





/*
                    selected = eventListFragment;
                    Intent intent = new Intent(InitActivity.this, SunnySearchActivity.class);
                    startActivity(intent);
*/
/*
                    Intent intent = new Intent(InitActivity.this, PanoramaActivity.class);
                    startActivity(intent);
*/
                }

                //if (position != 2){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                //}
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
