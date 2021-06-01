package com.GNSS.alpha;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.GNSS.alpha.data.DataStorage;
import com.GNSS.alpha.data.GNSS_Poster;
import com.GNSS.alpha.data.GNSS_Reader;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Handler;

public class MyApplication extends Application {
    public static MyApplication instance;
    public DataStorage ds;
    public GNSS_Poster poster;
    public GNSS_Reader reader;
    private final String TAG = "GNSS_APP";
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        while (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()== false) {
            Log.e(TAG, "no3g");
        }


        try {
            Runtime.getRuntime().exec("svc data disable && svc data enable").waitFor();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        try {
            ds = new DataStorage();
            poster = new GNSS_Poster(this.getApplicationContext(), ds);
            reader = new GNSS_Reader(ds);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return instance;
    }

}

