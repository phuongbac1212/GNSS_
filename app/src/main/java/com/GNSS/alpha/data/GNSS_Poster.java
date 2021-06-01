package com.GNSS.alpha.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.GNSS.alpha.MainActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Date;

public class GNSS_Poster implements Runnable {
    protected RequestQueue requestQueue;
    protected DataStorage ds;
    private final String url = "https://1cddb6899f9a5a.localhost.run/i"; //"http://112.137.134.7:5000/data";
    private final String id = "60b45f1cefbe0a23707d1fcd";
    private final String TAG = "GNSS_POSTER";
    protected Response.Listener rl;
    protected Response.ErrorListener el;

    public GNSS_Poster(Context cx, DataStorage ds) {
        requestQueue = Volley.newRequestQueue(cx);
        this.ds = ds;
        rl = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
            }
        };

        el = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Fail to post" + error.toString());
            }
        };
        new Thread(() -> {
            while (true) {

                if (!ds.isEmpty())
                    this.run();
            }
        }).start();
    }


    @Override
    public void run() {
        try {
            JSONObject jsonObject;
            jsonObject = new JSONObject();
            jsonObject.put("stationID", id);
            jsonObject.put("data", ds.popGNSS_Queue());
            jsonObject.put("time", new Date().getTime());
            Log.e(TAG, jsonObject.toString());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    rl,
                    el
            );
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            Log.e(TAG, "fail to create JSON FILE");
        }
    }
}
