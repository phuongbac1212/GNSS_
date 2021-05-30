package com.GNSS.alpha;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.allen.device.serialport.ISerialPortListener;
import org.allen.device.serialport.SerialPort;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public SerialPort mSerialPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }

        Log.e("GNSS_TAG", "start to read");
        try {
            String cmd = "/system/xbin/su -c chmod 777 /dev/ttyMT1";
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
            pr.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            while ((line=buf.readLine())!=null) {
                Log.e("GNSS_LOG" +
                        "", line);
            }

            InputStream inputStream = new FileInputStream("/dev/ttyMT1");
            Log.e("GNSS_TAG", "kidding?");
            byte[] readBuffer = new byte[500];
            StringBuilder sb = new StringBuilder();
            Log.e("GNSS_TAG", inputStream.available()+ "mean no file");

            new Thread(() -> {
                while (true) {
                    try {
                        while (inputStream.available() > 0) {
                            int numBytes = inputStream.read(readBuffer);
                            if (numBytes < 0)
                                break;
                            sb.append(new String(readBuffer, 0, numBytes));
//                            for(byte b: readBuffer) {
//                                sb.append(String.format("%02x", b));
//                            }
                            Log.e("GNSS_DATA", sb.toString());
                        }
                    }
                    catch (Exception e) {
                        Log.e("GNSS_ERROR", e.getMessage());
                    }
                }
            }).start();


        } catch (Exception e) {
            Log.e("GNSS_TAG", "read file fail: "+e.getMessage());
        }

//        new Thread(() -> {
//            while (true) {
//                Log.e("GNSS_LOG", "app is running");
//                //read uart data and post to internet
//            }
//        }).start();
    }


    public void postRequest(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://112.137.134.7:5000/data";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", "60b0a3ad49b01c15ecdc0617");
            jsonObject.put("data", data);
            long ts = new Date().getTime();

            jsonObject.put("time", ts);
            Log.e("GNSS_POST", ts+"");

        }
        catch (Exception e) {
            Log.e("GNSS_POST", "fail to create JSON FILE");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("GNSS_POST", response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GNSS_POST", "Fail to post" + error.toString());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}