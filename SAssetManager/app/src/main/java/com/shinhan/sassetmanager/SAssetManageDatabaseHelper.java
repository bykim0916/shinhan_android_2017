package com.shinhan.sassetmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by IC-INTPC-087112 on 2017-03-24.
 */

public class SAssetManageDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sassetmanage.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_MEMBER = "member";
    public static final String TABLE_NAME_EVENT_HIS = "event_his";
    public static final String TABLE_NAME_EVENT_IMAGE = "event_image";

    public SAssetManageDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_create_member = "create table " + TABLE_NAME_MEMBER + "(" +
                "member_id text PRIMARY KEY, " +
                "member_pwd text, member_name text, member_score integer)";  //테이블 생성 쿼리문

        String query_create_event_his = "create table " + TABLE_NAME_EVENT_HIS + "(" +
                "member_id text , " +
                "image_id text , " +
                "event_date text , " +
                "event_time text , PRIMARY KEY (member_id, image_id) )";  //테이블 생성 쿼리문

        String query_create_event_img = "create table " + TABLE_NAME_EVENT_IMAGE + "(" +
                "image_id integer PRIMARY KEY autoincrement, " +
                "image_lati double, image_longi double, event_g integer, image_score integer )";  //테이블 생성 쿼리문

        try {
            db.execSQL(query_create_member);  //쿼리 실행
            db.execSQL(query_create_event_his);  //쿼리 실행
            db.execSQL(query_create_event_img);  //쿼리 실행

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {//
            db.execSQL("drop table if exists " + TABLE_NAME_MEMBER);
            db.execSQL("drop table if exists " + TABLE_NAME_EVENT_HIS);
            db.execSQL("drop table if exists " + TABLE_NAME_EVENT_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        onCreate(db);   //DB파일 생성
    }
}
