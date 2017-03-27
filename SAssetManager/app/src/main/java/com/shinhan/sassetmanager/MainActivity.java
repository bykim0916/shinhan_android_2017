package com.shinhan.sassetmanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(MainActivity.this);
        SQLiteDatabase database = sAssetManageDatabaseHelper.getWritableDatabase();
        ContentValues values1 = new ContentValues();
        values1.put("member_id", "bykim0916");
        values1.put("member_pwd", "1234");
        values1.put("member_name", "김병윤");
        database.insert(SAssetManageDatabaseHelper.TABLE_NAME_MEMBER, null, values1);   //DB에 데이터 insert
/*
        ContentValues values3 = new ContentValues();
        values3.put("image_lati", 37.662325);
        values3.put("image_longi", 126.770711);
        database.insert(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null, values3);   //DB에 데이터 insert

        ContentValues values4 = new ContentValues();
        values4.put("image_lati", 37.652851);
        values4.put("image_longi", 126.774624);
        database.insert(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null, values4);   //DB에 데이터 insert

        ContentValues values5 = new ContentValues();
        values5.put("image_lati", 37.664287);
        values5.put("image_longi", 126.783786);
        database.insert(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null, values5);   //DB에 데이터 insert
*/
    }

    public void onLoginButtonClicked(View view) {

        boolean loginCheck = false;
        TextView tvMemberId = (TextView)findViewById(R.id.memberid);
        TextView tvMemberPwd = (TextView)findViewById(R.id.memberpwd);
        String strMemberId = tvMemberId.getText().toString();
        String strMemberPwd = tvMemberPwd.getText().toString();

        SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(MainActivity.this);
        SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery("select * from " + SAssetManageDatabaseHelper.TABLE_NAME_MEMBER, null);

        for (int i = 0 ; i < cursor.getCount() ; i++) {
            cursor.moveToNext();
            if (cursor.getString(0).equals(strMemberId)) {
                if (cursor.getString(1).equals(strMemberPwd)) {
                    loginCheck = true;
                    break;
                }
            }
        }

        Log.i("loginCheck", String.valueOf(loginCheck));

        if (loginCheck) {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            alt_bld.setMessage("김병윤 님 써니찾기 이벤트에 오신걸 환영합니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(MainActivity.this, InitActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.show();
        } else {
            AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
            alt_bld.setMessage("로그인 암호를 확인하세요").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Action for 'Yes' Button
                    dialog.cancel();
                }
            });
            AlertDialog alert = alt_bld.create();
            alert.setTitle("로그인 확인");
            alert.show();
        }
    }
}
