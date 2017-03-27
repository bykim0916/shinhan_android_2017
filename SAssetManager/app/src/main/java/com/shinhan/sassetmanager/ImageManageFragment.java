package com.shinhan.sassetmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ImageManageFragment extends Fragment {
/*
    public static interface ImageSelectionCallback {
        public void onImageSelected(int position);
    }

    public ImageSelectionCallback callback;
*/
    class ImageItem {
        int imageId;
        double imageLati;
        double imageLongi;
        ImageItem(int imageId, double imageLati, double imageLongi) {
            this.imageId = imageId; this.imageLati = imageLati; this.imageLongi = imageLongi;
        }
    }

    ArrayList<ImageItem> ImageItems = new ArrayList<ImageItem>();
    ImageListAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_image_manage, container, false);
/*
        ImageItems.add(new ImageItem(1,37.662325,126.770711));
        ImageItems.add(new ImageItem(2,37.652851,126.774624));
        ImageItems.add(new ImageItem(3,37.664287,126.783786));
*/
        //최초에 모든 이미지 목록을 조회함
        ImageItems.clear();
        SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
        SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null);
        for (int i = 0 ; i < cursor.getCount() ; i++) {
            cursor.moveToNext();
            ImageItems.add(new ImageItem(cursor.getInt(0),cursor.getDouble(1),cursor.getDouble(2)));
        }

        //조회 버튼 클릭시
        Button button1 = (Button)rootView.findViewById(R.id.selectbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button1click" , "$$$$$$$$$$$$$$$$$button1click$$$$$$$$$$$$$$$$$$");
                ImageItems.clear();
                EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                if (editText1.getText().toString().isEmpty()) {
                    //아이디가 있는 경우 해당 아이디의 이미지를 조회하고 아이디가 없을 경우 전체 목록을 조회함
                    SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                    SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();
                    Cursor cursor = database.rawQuery("select * from " + SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null);
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        ImageItems.add(new ImageItem(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
                    }
                } else {
                    //선택한 건의 데이터를 화면에 조회함
                    SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                    SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();
                    String sqlSelect = "" +
                            "select image_id, image_lati, image_longi " +
                            "from event_image " +
                            "where image_id = '" + editText1.getText().toString() + "' ";
                    Cursor cursor = database.rawQuery(sqlSelect, null);
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        ImageItems.add(new ImageItem(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
                    }
                }

                ListView listView = (ListView)rootView.findViewById(R.id.listview);
                listAdapter = new ImageListAdapter(getContext());
                listView.setAdapter(listAdapter);

            }
        });

        //수정 버튼 클릭시
        Button updateButton = (Button)rootView.findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("updateButtonclick" , "$$$$$$$$$$$$$$$$$updateButtonclick$$$$$$$$$$$$$$$$$$");
                ImageItems.clear();

                EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                EditText editText2 = (EditText)rootView.findViewById(R.id.imagelati);
                EditText editText3 = (EditText)rootView.findViewById(R.id.imagelongi);

                SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                SQLiteDatabase database = sAssetManageDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("image_lati", Double.parseDouble(editText2.getText().toString()));
                values.put("image_longi", Double.parseDouble(editText3.getText().toString()));
                String[] whereArgs = {editText1.getText().toString()};
                int rowAffected = database.update(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, values, "image_id = ?", whereArgs );   //DB에 데이터 update
                Log.i("rowAffected", "#############==>" + rowAffected);

                database = sAssetManageDatabaseHelper.getReadableDatabase();
                String sqlSelect = "" +
                        "select image_id, image_lati, image_longi " +
                        "from event_image " +
                        "where image_id = '" + editText1.getText().toString() + "' ";
                Cursor cursor = database.rawQuery(sqlSelect, null);
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    ImageItems.add(new ImageItem(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
                }

                ListView listView = (ListView)rootView.findViewById(R.id.listview);
                listAdapter = new ImageListAdapter(getContext());
                listView.setAdapter(listAdapter);

            }
        });

        //입력 버튼 클릭시
        Button insertButton = (Button)rootView.findViewById(R.id.insertbutton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("insertButtonclick" , "$$$$$$$$$$$$$$$$$insertButtonclick$$$$$$$$$$$$$$$$$$");
                ImageItems.clear();

                //EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                EditText editText2 = (EditText)rootView.findViewById(R.id.imagelati);
                EditText editText3 = (EditText)rootView.findViewById(R.id.imagelongi);

                SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                SQLiteDatabase database = sAssetManageDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("image_lati", Double.parseDouble(editText2.getText().toString()));
                values.put("image_longi", Double.parseDouble(editText3.getText().toString()));
                values.put("event_g", 1);
                values.put("image_score", 1);
                database.insert(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null, values);   //DB에 데이터 insert

                database = sAssetManageDatabaseHelper.getReadableDatabase();
                Cursor cursor = database.rawQuery("select * from " + SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, null);
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    ImageItems.add(new ImageItem(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
                }

                ListView listView = (ListView)rootView.findViewById(R.id.listview);
                listAdapter = new ImageListAdapter(getContext());
                listView.setAdapter(listAdapter);

            }
        });

        //삭제 버튼 클릭시
        Button deleteButton = (Button)rootView.findViewById(R.id.deletebutton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("deleteButtonclick" , "$$$$$$$$$$$$$$$$$deleteButtonclick$$$$$$$$$$$$$$$$$$");
                ImageItems.clear();

                EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                //EditText editText2 = (EditText)rootView.findViewById(R.id.imagelati);
                //EditText editText3 = (EditText)rootView.findViewById(R.id.imagelongi);

                SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                SQLiteDatabase database = sAssetManageDatabaseHelper.getWritableDatabase();
                String[] whereArgs = {editText1.getText().toString()};
                int rowAffected = database.delete(SAssetManageDatabaseHelper.TABLE_NAME_EVENT_IMAGE, "image_id = ?", whereArgs );   //DB에 데이터 delete
                Log.i("rowAffected", "#############==>" + rowAffected);

                database = sAssetManageDatabaseHelper.getReadableDatabase();
                String sqlSelect = "" +
                        "select image_id, image_lati, image_longi " +
                        "from event_image " +
                        "where image_id = '" + editText1.getText().toString() + "' ";
                Cursor cursor = database.rawQuery(sqlSelect, null);
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    ImageItems.add(new ImageItem(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2)));
                }

                ListView listView = (ListView)rootView.findViewById(R.id.listview);
                listAdapter = new ImageListAdapter(getContext());
                listView.setAdapter(listAdapter);

            }
        });


        ListView listView = (ListView)rootView.findViewById(R.id.listview);
        listAdapter = new ImageListAdapter(getContext());
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("clickevent", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$clickevent#############");

                EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                EditText editText2 = (EditText)rootView.findViewById(R.id.imagelati);
                EditText editText3 = (EditText)rootView.findViewById(R.id.imagelongi);

                TextView textView1 = (TextView)view.findViewById(R.id.dataItem01);
                TextView textView2 = (TextView)view.findViewById(R.id.dataItem02);
                TextView textView3 = (TextView)view.findViewById(R.id.dataItem03);

                editText1.setText(textView1.getText());
                editText2.setText(textView2.getText());
                editText3.setText(textView3.getText());
/*
                if (callback != null) {
                    callback.onImageSelected(position);
                }
*/
            }
        });

        return rootView;
    }
/*
    public void onSearchButtonClicked(View view) {
        EditText editText = (EditText)findViewById(R.id.input01);
        String urlString = editText.getText().toString();
        if (urlString.indexOf("http") != -1) {
            new LoadXML().execute(urlString);
        }
    }
*/
    class ImageListAdapter extends ArrayAdapter {
        public ImageListAdapter(Context context) {
            super(context, R.layout.listitem, ImageItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.listitem, null, true);
            }

            TextView dataItem01 = (TextView)view.findViewById(R.id.dataItem01);
            TextView dataItem02 = (TextView)view.findViewById(R.id.dataItem02);
            TextView dataItem03 = (TextView)view.findViewById(R.id.dataItem03);

            dataItem01.setText(String.valueOf(ImageItems.get(position).imageId));
            dataItem02.setText(String.valueOf(ImageItems.get(position).imageLati));
            dataItem03.setText(String.valueOf(ImageItems.get(position).imageLongi));

            return view;
        }
    }

}
