package com.shinhan.linearlayoutexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

    }

    public void onButtonClicked(View v) {
        Button button = (Button)v;
        ImageView imageview1 = (ImageView)findViewById(R.id.imageview1);
        ImageView imageview2 = (ImageView)findViewById(R.id.imageview2);
        if (button.getId() == R.id.button1) {
            imageview1.setBackgroundResource(R.drawable.twice2);
            imageview2.setBackgroundResource(R.drawable.twice2);
        } else if (button.getId() == R.id.button2) {
            imageview1.setBackgroundResource(R.drawable.twice3);
            imageview2.setBackgroundResource(R.drawable.twice3);
        }

    }
}
