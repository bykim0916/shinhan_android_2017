package com.shinhan.googlenewsexam;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    private static String rssUrl = "http://api.sbs.co.kr/xml/news/rss.jsp?pmDiv=entertainment";

    class RSSNewsItem {
        String title, link, description, pubDate, author, category;
        RSSNewsItem(String title, String link, String description, String pubDate, String author, String category) {
            this.title = title; this.link = link; this.description = description; this.pubDate = pubDate;
            this.author = author; this.category = category;
        }
    }

    ArrayList<RSSNewsItem> rssNewsItems = new ArrayList<RSSNewsItem>();
    RSSListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssNewsItems.add(new RSSNewsItem("제목1","http://api.sbs.co.kr/xml/news/rss.jsp?pmDiv=entertainment","설명","날짜","작성자","카테고리"));
        rssNewsItems.add(new RSSNewsItem("제목2","https://m.naver.com","설명","날짜","작성자","카테고리"));
        rssNewsItems.add(new RSSNewsItem("제목3","https://m.naver.com","설명","날짜","작성자","카테고리"));

        EditText editText = (EditText)findViewById(R.id.input01);
        editText.setText(rssUrl);

        ListView listView = (ListView)findViewById(R.id.listview);
        listAdapter = new RSSListAdapter(MainActivity.this);
        listView.setAdapter(listAdapter);
    }

    class RSSListAdapter extends ArrayAdapter {
        public RSSListAdapter(Context context) {
            super(context, R.layout.listitem, rssNewsItems);
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
            WebView dataItem04 = (WebView)view.findViewById(R.id.dataItem04);

            dataItem01.setText(rssNewsItems.get(position).title);
            dataItem02.setText(rssNewsItems.get(position).pubDate);
            dataItem03.setText(rssNewsItems.get(position).category);

            dataItem04.loadData(rssNewsItems.get(position).description, "text/html; charset=utf-8","utf-8");

            return view;
        }
    }

    public void onButtonClicked(View view) {
        EditText editText = (EditText)findViewById(R.id.input01);
        String urlString = editText.getText().toString();
        if (urlString.indexOf("http") != -1) {
            new LoadXML().execute(urlString);
        }
    }

    public void onTitleClicked(View view) {

        TextView dataItem01 = (TextView)view.findViewById(R.id.dataItem01);
        String title = dataItem01.getText().toString();

        for (int i = 0 ; i < listAdapter.getCount() ; i++) {
            if (rssNewsItems.get(i).title.equals(title)) {
                Log.i("index", i + "$$$$$" + title + "$$$$$$$$$$$$");

                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rssNewsItems.get(i).link.toString()));
                startActivity(myIntent);
                break;
            }
        }


    }

    class LoadXML extends AsyncTask<String, String, String> {

        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("뉴스 RSS 요청 중...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... params) { //실제 통신이 처리되는 부분
            StringBuilder output = new StringBuilder();
            try {
                //URL url = new URL("https://m.naver.com");
                //URL url = new URL("https://news.google.co.kr/news/feeds?pz=1&cf=all&ned=kr&hl=ko&topic=e&output=rss");
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                if (connection != null) {
                    connection.setConnectTimeout(10000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    int resCode = connection.getResponseCode();
                    /////////////////////////////////////////////////////////
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();

                    InputStream inputStream = connection.getInputStream();
                    Document document = documentBuilder.parse(inputStream);
                    Log.d("tAG", "@@@@@@@@@@@@@@##############################");
                    int count = processDocument(document);
                    Log.i("count",count+"##############################");
/*
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while (true) {
                        line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }
                    bufferedReader.close();
                    */
                    connection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return output.toString();
        }
    }

    private int processDocument(Document document) {    //XML 문서 파싱
        int count = 0;

        Log.i("count", "########processDocument################");

        //전체리스트 초기화
        rssNewsItems.clear();
        Element element = document.getDocumentElement();
        NodeList nodeList = element.getElementsByTagName("item");
        if (nodeList != null && (nodeList.getLength() > 0)) {
            for (int i = 0 ; i < nodeList.getLength() ; i++) {
                RSSNewsItem newsItem = dissectNode(nodeList, i);
                if (newsItem != null) {
                    //if (newsItem.description.indexOf("결혼") > -1) {
                        rssNewsItems.add(newsItem);
                        count++;
                   // }
                }
            }
        }
        return count;
    }

    private RSSNewsItem dissectNode(NodeList nodeList, int index) { //특정 아이템 정보 추출
        RSSNewsItem newsItem = null;
        //newsItem = new RSSNewsItem("제목","https://m.naver.com","설명","날짜","작성자","카테고리");

        try {
            Element entry = (Element)nodeList.item(index);
            Element title = (Element)entry.getElementsByTagName("title").item(0);
            Element link = (Element)entry.getElementsByTagName("link").item(0);
            Element description = (Element)entry.getElementsByTagName("description").item(0);
            Element pubDate = (Element)entry.getElementsByTagName("pubDate").item(0);
            Element author = (Element)entry.getElementsByTagName("author").item(0);
            Element category = (Element)entry.getElementsByTagName("category").item(0);
            String titleValue = getElementString(title);
            String linkValue = getElementString(link);
            String descriptionValue = getElementString(description);
            String pubDateValue = getElementString(pubDate);
            String authorValue = getElementString(author);
            String categoryValue = getElementString(category);

            newsItem = new RSSNewsItem(titleValue, linkValue, descriptionValue, pubDateValue, authorValue, categoryValue);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newsItem;
    }

    private String getElementString(Element element) {
        String value = null;
        if (element != null) {
            Node firstChild = element.getFirstChild();
            if (firstChild != null) {
                value = firstChild.getNodeValue();
            }
        }
        return value;
    }
}
