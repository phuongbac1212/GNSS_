package com.GNSS.alpha.data;

import android.content.Context;
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

public class GNSS_Poster implements Runnable{
    protected       RequestQueue requestQueue ;
    protected       JSONObject jsonObject;
    protected       DataStorage ds;
    private final   String url =  "http://112.137.134.7:5000/data";
    private final   String id = "60b0a3ad49b01c15ecdc0617";
    private final   String TAG = "GNSS_POSTER";
    protected       Response.Listener rl;
    protected       Response.ErrorListener el;

    public GNSS_Poster(Context cx, DataStorage ds) {
        requestQueue = Volley.newRequestQueue(cx);
        this.ds = ds;
        jsonObject = new JSONObject();
        rl = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
            }
        };

        el = new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Fail to post" + error.toString());
            }
        };
        new Thread(() -> {
            while (true) {
                this.run();
            }
        }).start();
    }


    @Override
    public void run() {

        try {
            jsonObject.put("_id", id);
            jsonObject.put("data", ds.popGNSS_Queue());
            jsonObject.put("time", new Date().getTime());
        }
        catch (Exception e) {
            Log.e(TAG, "fail to create JSON FILE");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                rl,
                el
        );
        requestQueue.add(jsonObjectRequest);
    }
}
