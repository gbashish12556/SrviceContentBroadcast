package com.test.ashish.androidpractice;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyBoundService extends Service {


    public static final String SERVICE_DEMO = "Service Demo";
    private int mRandomNo;
    private boolean mIsRandomNoGeneratorIsOn;

    private final int MIN = 0;
    private final int MAX = 100;


    class MyServiceBinder extends Binder{
        public MyBoundService getService(){
            return MyBoundService.this;
        }
    }

    private IBinder MyServiceeBinder = new MyServiceBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(SERVICE_DEMO, "Service Started");
        mIsRandomNoGeneratorIsOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGeneratorOn();
            }
        }).start();
        return  START_STICKY;
    }

    public void startRandomGeneratorOn(){
        while(mIsRandomNoGeneratorIsOn){
            try {
                Thread.sleep(2000);
                if(mIsRandomNoGeneratorIsOn){
                    mRandomNo = new Random().nextInt(MAX)+MIN;
                    Log.i(SERVICE_DEMO, String.valueOf(mRandomNo));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void randomNoGenerator(){
        mIsRandomNoGeneratorIsOn = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(SERVICE_DEMO ,"In onUnbind");
        return super.onUnbind(intent);
    }

    public int getRandomNo(){
        return mRandomNo;
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(SERVICE_DEMO, "In OnBind");
        return MyServiceeBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(SERVICE_DEMO ,"In onRebind");
    }

    @Override
    public void onDestroy() {
        mIsRandomNoGeneratorIsOn = false;
        super.onDestroy();
    }
}

