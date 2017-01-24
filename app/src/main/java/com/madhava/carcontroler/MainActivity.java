package com.madhava.carcontroler;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SeekBar bar;
    Button forward , reverse;
    Sensor accel;
    SensorManager sm;
    TextView val ,result;
    static int y , old;
    static float yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL);
        forward = (Button)findViewById(R.id.forward);
        reverse = (Button)findViewById(R.id.reverse);
        bar = (SeekBar)findViewById(R.id.bar);
        val=(TextView)findViewById(R.id.val);
        result=(TextView)findViewById(R.id.result);


        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    new DownloadWebpageTask().execute("brf");

                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    new DownloadWebpageTask().execute("brs");
                }
                return  true;
            }
        });

            reverse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    new DownloadWebpageTask().execute("brb");
                }
                else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    new DownloadWebpageTask().execute("brs");
                }
                return  true;
            }
        });

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
       float yy = sensorEvent.values[1];;


        if(sensorEvent.values[0]<0) {
            yy *=-1;
        }

        if(yy<-5.0){
            yy = -5;
        }
        if(yy>5.0){
            yy = 5;
        }

        int y = (int)yy;

        if(y<=-4){
            y = -4;
        }else if(y<4){
            y = 0;
        }else{
            y = 4;
        }

        if(y!=old) {
            old = y;
            if (y == -4) {
                new DownloadWebpageTask().execute("fl");
            } else if (y == 4) {
                new DownloadWebpageTask().execute("fr");
            } else {
                new DownloadWebpageTask().execute("fs");
            }
        }
        bar.setProgress((int)((yy + 5.0) * 10.0));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                URL url = new URL("http://192.168.10.1/?" + urls[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.getInputStream();

                Log.d("sucess","mandy");
                    return null;

                    // Makes sure that the InputStream is closed after the app is
                    // finished using it.

            } catch (Exception e) {
                Log.d("error",e.toString());
            }
            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }
}
