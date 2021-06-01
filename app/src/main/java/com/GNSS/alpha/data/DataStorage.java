package com.GNSS.alpha.data;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class DataStorage {
    private Queue<String> GNSS_Queue;
    private File fout;
    private FileWriter writer;
    private final String TAG = "GNSS_STORAGE";
    public DataStorage() throws IOException {
        GNSS_Queue = new LinkedList<String>();

//        fout = new File("GNSS_DATA_log.txt");
//        writer = new FileWriter(fout);
    }

    public String popGNSS_Queue() {
        return this.GNSS_Queue.poll();
    }

    public boolean isEmpty() {
        return GNSS_Queue.isEmpty();
    }

    public void putGNSS_Queue(String data) throws IOException {
//        Log.e(TAG, data);
        this.GNSS_Queue.add(data);

//        writer.append(data);
//        writer.flush();
//        writer.close();
    }
}
