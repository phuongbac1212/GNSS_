package com.GNSS.alpha;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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

        try {
            ds = new DataStorage();
            Log.e(TAG, "reach here1  ^^");
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

