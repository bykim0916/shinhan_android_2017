package com.shinhan.sassetmanager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventHistSearchFragment extends Fragment {

    class EventHistItem {

        String memberId;
        int imageId;
        String memberName;
        int memberScore;
        int eventG;
        double imageLati;
        double imageLongi;
        int imageScore;
        String eventDate;
        String eventTime;

        EventHistItem(
                String memberId,
                int imageId,
                String memberName,
                int memberScore,
                int eventG,
                double imageLati,
                double imageLongi,
                int imageScore,
                String eventDate,
                String eventTime) {

            this.memberId = memberId;
            this.imageId = imageId;
            this.memberName = memberName;
            this.memberScore = memberScore;
            this.eventG = eventG;
            this.imageLati = imageLati;
            this.imageLongi = imageLongi;
            this.imageScore = imageScore;
            this.eventDate = eventDate;
            this.eventTime = eventTime;

        }
    }

    ArrayList<EventHistItem> EventHistItems = new ArrayList<EventHistItem>();
    EventHistSearchAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_event_hist_search, container, false);

        String memberId = "bykim0916";
        EditText editText = (EditText)rootView.findViewById(R.id.memberid);
        editText.setText(memberId);
        //최초에 로그인한 사용자의 이벤트이력을 조회함
        EventHistItems.clear();
        SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
        SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();
        memberId = editText.getText().toString();
        String sqlSelect = "" +
                "select a.member_id, a.image_id, b.member_name, b.member_score, c.event_g, c.image_lati, c.image_longi " +
                "      ,c.image_score, a.event_date, a.event_time " +
                "from event_his a, member b, event_image c " +
                "where 1 = 1 " +
                "  and a.member_id = b.member_id " +
                "  and a.image_id = c.image_id " +
                "  and a.member_id = '" + memberId + "' ";
        Cursor cursor = database.rawQuery(sqlSelect, null);
        for (int i = 0 ; i < cursor.getCount() ; i++) {
            cursor.moveToNext();
            EventHistItems.add(new EventHistSearchFragment.EventHistItem(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getDouble(5),
                    cursor.getDouble(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9)
                    )
            );

            Log.i("cursor.getString(0)", "##############" + cursor.getString(0) + "");
            Log.i("cursor.getInt(1)", "##############" + cursor.getInt(1) + "");
            Log.i("cursor.getString(2)", "##############" + cursor.getString(2) + "");
            Log.i("cursor.getInt(3)", "##############" + cursor.getInt(3) + "");
            Log.i("cursor.getInt(4)", "##############" + cursor.getInt(4) + "");
            Log.i("cursor.getDouble(5)", "##############" + cursor.getDouble(5) + "");
            Log.i("cursor.getDouble(6)", "##############" + cursor.getDouble(6) + "");
            Log.i("cursor.getInt(7)", "##############" + cursor.getInt(7) + "");
            Log.i("cursor.getString(8)", "##############" + cursor.getString(8) + "");
            Log.i("cursor.getString(9)", "##############" + cursor.getString(9) + "");
        }

        Log.i("count", "##############" + cursor.getCount() + "");
        Log.i("query", "##############" + sqlSelect + "");
        Log.i("EventHistItems", "##############" + EventHistItems.toString() + "");


        //조회 버튼 클릭시
        Button button1 = (Button)rootView.findViewById(R.id.selectbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("button1click" , "$$$$$$$$$$$$$$$$$button1click$$$$$$$$$$$$$$$$$$");
                EventHistItems.clear();
                EditText editText1 = (EditText)rootView.findViewById(R.id.memberid);
                //if (editText1.getText().toString().isEmpty()) {
                    //아이디가 있는 경우 해당 아이디의 이벤트이력을 조회하고 아이디가 없을 경우 전체 목록을 조회함
                    SAssetManageDatabaseHelper sAssetManageDatabaseHelper = new SAssetManageDatabaseHelper(getContext());
                    SQLiteDatabase database = sAssetManageDatabaseHelper.getReadableDatabase();
                    String memberId = editText1.getText().toString();
                    String sqlSelect = "" +
                            "select a.member_id, a.image_id, b.member_name, b.member_score, c.event_g, c.image_lati, c.image_longi " +
                            "      ,c.image_score, a.event_date, a.event_time " +
                            "from event_his a, member b, event_image c " +
                            "where 1 = 1 " +
                            "  and a.member_id = b.member_id " +
                            "  and a.image_id = c.image_id " +
                            "  and a.member_id = '" + memberId + "' ";
                    Cursor cursor = database.rawQuery(sqlSelect, null);
                    for (int i = 0 ; i < cursor.getCount() ; i++) {
                        cursor.moveToNext();
                        EventHistItems.add(new EventHistSearchFragment.EventHistItem(
                                        cursor.getString(0),
                                        cursor.getInt(1),
                                        cursor.getString(2),
                                        cursor.getInt(3),
                                        cursor.getInt(4),
                                        cursor.getDouble(5),
                                        cursor.getDouble(6),
                                        cursor.getInt(7),
                                        cursor.getString(8),
                                        cursor.getString(9)
                                )
                        );
                    }

                //}

                GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
                listAdapter = new EventHistSearchAdapter(getContext());
                gridView.setAdapter(listAdapter);

            }
        });

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview);
        listAdapter = new EventHistSearchAdapter(getContext());
        gridView.setAdapter(listAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("clickevent", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$clickevent#############");
/*
                EditText editText1 = (EditText)rootView.findViewById(R.id.imageid);
                EditText editText2 = (EditText)rootView.findViewById(R.id.imagelati);
                EditText editText3 = (EditText)rootView.findViewById(R.id.imagelongi);

                TextView textView1 = (TextView)view.findViewById(R.id.dataItem01);
                TextView textView2 = (TextView)view.findViewById(R.id.dataItem02);
                TextView textView3 = (TextView)view.findViewById(R.id.dataItem03);

                editText1.setText(textView1.getText());
                editText2.setText(textView2.getText());
                editText3.setText(textView3.getText());
*/
/*
                if (callback != null) {
                    callback.onImageSelected(position);
                }
*/
            }
        });

        return rootView;
    }

    class EventHistSearchAdapter extends ArrayAdapter {
        public EventHistSearchAdapter(Context context) {
            super(context, R.layout.griditem, EventHistItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.griditem, null, true);
            }

            //TextView memberid = (TextView)view.findViewById(R.id.memberid);
            //TextView imageid = (TextView)view.findViewById(R.id.imageid);
            //TextView membername = (TextView)view.findViewById(R.id.membername);
            //TextView memberscore = (TextView)view.findViewById(R.id.memberscore);
            //TextView eventg = (TextView)view.findViewById(R.id.eventg);
            TextView imagelati = (TextView)view.findViewById(R.id.imagelati);
            TextView imagelongi = (TextView)view.findViewById(R.id.imagelongi);
            //TextView imagescore = (TextView)view.findViewById(R.id.imagescore);
            TextView eventdate = (TextView)view.findViewById(R.id.eventdate);
            //TextView eventtime = (TextView)view.findViewById(R.id.eventtime);

            Log.i("imageLati", "@@@@@@@@@@@@@"+ EventHistItems.get(position).imageLati + "@@@@@@@@@");
            Log.i("imageLongi", "@@@@@@@@@@@@@"+ EventHistItems.get(position).imageLongi + "@@@@@@@@@");
            Log.i("eventDate", "@@@@@@@@@@@@@"+ EventHistItems.get(position).eventDate + "@@@@@@@@@");
            Log.i("eventTime", "@@@@@@@@@@@@@"+ EventHistItems.get(position).eventTime + "@@@@@@@@@");
            Log.i("imageLati", "@@@@@@@@@@@@@"+ String.valueOf(EventHistItems.get(position).imageLati) + "@@@@@@@@@");
            Log.i("imageLongi", "@@@@@@@@@@@@@"+ String.valueOf(EventHistItems.get(position).imageLongi) + "@@@@@@@@@");
            Log.i("eventDate", "@@@@@@@@@@@@@"+ String.valueOf(EventHistItems.get(position).eventDate) + "@@@@@@@@@");
            Log.i("eventTime", "@@@@@@@@@@@@@"+ String.valueOf(EventHistItems.get(position).eventTime) + "@@@@@@@@@");
            Log.i("imageLati", "@@@@@@@@@@@@@"+ imagelati.getText() + "@@@@@@@@@");
            Log.i("imageLongi", "@@@@@@@@@@@@@"+ imagelongi.getText() + "@@@@@@@@@");
            Log.i("eventDate", "@@@@@@@@@@@@@"+ eventdate.getText() + "@@@@@@@@@");
            //Log.i("eventTime", "@@@@@@@@@@@@@"+ eventtime.getText() + "@@@@@@@@@");

            //memberid.setText(String.valueOf(EventHistItems.get(position).memberId));
            //imageid.setText(String.valueOf(EventHistItems.get(position).imageId));
            //membername.setText(String.valueOf(EventHistItems.get(position).memberName));
            //memberscore.setText(String.valueOf(EventHistItems.get(position).memberScore));
            //eventg.setText(String.valueOf(EventHistItems.get(position).eventG));
            imagelati.setText("위도 : " + String.valueOf(EventHistItems.get(position).imageLati));
            imagelongi.setText("경도 : " + String.valueOf(EventHistItems.get(position).imageLongi));
            //imagescore.setText(String.valueOf(EventHistItems.get(position).imageScore));
            eventdate.setText("일시 : " + String.valueOf(EventHistItems.get(position).eventDate));
            //eventtime.setText(String.valueOf(EventHistItems.get(position).eventTime));

            Log.i("imageLati111", "@@@@@@@@@@@@@"+ imagelati.getText() + "@@@@@@@@@");
            Log.i("imageLongi111", "@@@@@@@@@@@@@"+ imagelongi.getText() + "@@@@@@@@@");
            Log.i("eventDate111", "@@@@@@@@@@@@@"+ eventdate.getText() + "@@@@@@@@@");
            //Log.i("eventTime111", "@@@@@@@@@@@@@"+ eventtime.getText() + "@@@@@@@@@");

            return view;
        }
    }
}
