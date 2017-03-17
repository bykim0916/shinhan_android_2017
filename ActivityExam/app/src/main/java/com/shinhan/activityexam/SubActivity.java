package com.shinhan.activityexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();
        String string = intent.getStringExtra("String");
        EditText editText = (EditText)findViewById(R.id.edittext);
        editText.setText(string);
    }

    public void onCloseButtonClicked(View v) {
        Intent intent = new Intent();
        EditText editText = (EditText) findViewById(R.id.edittext);
        intent.putExtra("Result", editText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
