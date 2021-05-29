package com.GNSS.alpha;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }
        new Thread(() -> {
            while (true) {
                Log.e("GNSS_LOG", "app is running");
                //read uart data and post to internet
            }
        }).start();
    }

    public void crashMe(View v) {
        throw new NullPointerException();
    }

}
