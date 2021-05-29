package com.GNSS.alpha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }
        postRequest();
        postRequest();

//        new Thread(() -> {
//            while (true) {
//                Log.e("GNSS_LOG", "app is running");
//                //read uart data and post to internet
//            }
//        }).start();
    }

    public void crashMe(View v) {
        throw new NullPointerException();
    }



    public void postRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://112.137.134.7:5000/data";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", "60b0a3ad49b01c15ecdc0617");
            jsonObject.put("data", "60b0a3ad49b01c15ecdc061760b0a3ad49b01c15ecdc0617");
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