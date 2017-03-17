package com.shinhan.helloworld;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.button);
        TextView textview = (TextView)findViewById(R.id.textview);

        textview.setText("안녕, 신한은행!");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "버튼 클릭!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onButton2Clicked(View v) {
        //Button button = (Button)v;
        //Toast.makeText(MainActivity.this, button.getText(), Toast.LENGTH_LONG).show();
        Toast.makeText(MainActivity.this, "두번째 버튼 클릭", Toast.LENGTH_LONG).show();
    }
    public void onButton3Clicked(View v) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.shinhan.com"));
        startActivity(myIntent);
    }
    public void onButton4Clicked(View v) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-7379-0916"));
        startActivity(myIntent);
    }
}
