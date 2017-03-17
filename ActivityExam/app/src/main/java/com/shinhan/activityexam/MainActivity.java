package com.shinhan.activityexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButton1Clicked(View v) {
        Log.i("onButton1~~~", "good");
        EditText editText = (EditText)findViewById(R.id.edittext);
        String string = editText.getText().toString();
        Log.i("string", string);
        Intent myIntent = new Intent(MainActivity.this, SubActivity.class );
        myIntent.putExtra("String", string);
        startActivityForResult(myIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("Result");
                EditText editText = (EditText)findViewById(R.id.edittext);
                editText.setText(result);
            }
        }
    }
}
