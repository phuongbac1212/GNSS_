package com.GNSS.alpha.data;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GNSS_Reader implements Runnable {

    protected InputStream reader;
    protected StringBuilder buff;
    protected DataStorage ds;

    private final String TAG = "GNSS_READER";
    private byte[] readBuffer;
    public GNSS_Reader(DataStorage ds) throws FileNotFoundException {
        reader = new FileInputStream("/dev/ttyMT1");
        readBuffer = new byte[500];
        this.ds = ds;

        new Thread(() -> {
            while (true) {
                this.run();
            }
        }).start();
    }


    @Override
    public void run() {

        while (true) {
            try {
                while (reader.available() > 0) {
                    buff = new StringBuilder();
                    int numBytes = reader.read(readBuffer);
                    if (numBytes < 0)
                        break;
//                        buff.append(new String(readBuffer, 0, numBytes));
                    for(int i =0 ;i<numBytes; i++) {
                        buff.append(String.format("%02x", readBuffer[i]));
                    }
                    ds.putGNSS_Queue(buff.toString());
                }
            }
            catch (Exception e) {
                Log.e("GNSS_ERROR", e.getMessage());
            }
        }
    }
}
