package com.shinhan.sassetmanager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }
}
