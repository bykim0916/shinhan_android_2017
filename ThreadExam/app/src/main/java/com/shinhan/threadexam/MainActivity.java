package com.shinhan.threadexam;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressHandler progressHandler = new ProgressHandler();
    Handler handler = new Handler();
    ProgressRunnable progressRunnable = new ProgressRunnable();
    boolean isRunning1 = true;
    boolean isRunning2 = true;
    boolean isRunning4 = true;
    ProgressBar progressBar1, progressBar2, progressBar4;
    TextView textView1, textView2, textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView)findViewById(R.id.textview1);
        textView2 = (TextView)findViewById(R.id.textview2);
        textView4 = (TextView)findViewById(R.id.textview4);
        progressBar1 = (ProgressBar)findViewById(R.id.progressbar1);
        progressBar2 = (ProgressBar)findViewById(R.id.progressbar2);
        progressBar4 = (ProgressBar)findViewById(R.id.progressbar4);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //프로그레스바 초기화
        progressBar1.setProgress(0);
        progressBar2.setProgress(0);
        progressBar4.setProgress(0);
/*
        Thread thread = new Thread(new Runnable() { //스레드 정의
            @Override
            public void run() { //스레드에서 실행되는 영역 (메인UI에 접근 불가, Toast도 안됨)
                try {

                    for (int i = 0 ; i < 100 && isRunning; i++) {
                        Thread.sleep(1000);
                        //1. 핸들러를 이용한 메세지 전송 방법
                        Message msg = progressHandler.obtainMessage();
                        progressHandler.sendMessage(msg);

                        //2. Runnable 객체를 실행하는 방법
                        handler.post(progressRunnable);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        isRunning = true;
        thread.start();
*/

        Thread thread1 = new Thread(new Runnable() { //스레드 정의
            @Override
            public void run() { //스레드에서 실행되는 영역 (메인UI에 접근 불가, Toast도 안됨)
                try {

                    for (int i = 0 ; i < 20 && isRunning1; i++) {
                        Thread.sleep(1000);
                        //1. 핸들러를 이용한 메세지 전송 방법
                        Message msg = progressHandler.obtainMessage();
                        progressHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        isRunning1 = true;
        thread1.start();

        Thread thread2 = new Thread(new Runnable() { //스레드 정의
            @Override
            public void run() { //스레드에서 실행되는 영역 (메인UI에 접근 불가, Toast도 안됨)
                try {

                    for (int i = 0 ; i < 100 && isRunning2; i++) {
                        Thread.sleep(200);

                        //2. Runnable 객체를 실행하는 방법
                        handler.post(progressRunnable);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        isRunning2 = true;
        thread2.start();

        isRunning4 = true;
        new ProgressTask().execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning1 = false;
        isRunning2 = false;
        isRunning4 = false;
    }

    public class ProgressHandler extends Handler {  //스레드 대신 메인UI 접근
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);

            progressBar1.incrementProgressBy(5);
            if (progressBar1.getProgress() == progressBar1.getMax()) {
                textView1.setText("Done");
            } else {
                textView1.setText("Working..." + progressBar1.getProgress());
            }

        }
    }

    public class ProgressRunnable implements Runnable {
        @Override
        public void run() {
            progressBar2.incrementProgressBy(1);
            if (progressBar2.getProgress() == progressBar2.getMax()) {
                textView2.setText("Done");
            } else {
                textView2.setText("Working..." + progressBar2.getProgress());
            }
        }
    }

    public class ProgressTask extends AsyncTask<Integer, Integer, Integer> {
        int value = 0;
        @Override
        protected Integer doInBackground(Integer... params) {   //UI접근하면 안됨

            for (int i = 0 ; i < 10 && isRunning4; i++) {
                value+=10;

                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                publishProgress(value); //onProgressUpdate 호출
            }
            return null;
        }

        @Override
        protected void onPreExecute() {     //백그라운드 작업 전에 호출
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {     //백그라운드 작업 후에 호출
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {    //작업중 필요시 호출
            super.onProgressUpdate(values);
            progressBar4.setProgress(values[0]);
            if (progressBar4.getProgress() == progressBar4.getMax()) {
                textView4.setText("Done");
            } else {
                textView4.setText("Working..." + values[0]);
            }


        }
    }
}
