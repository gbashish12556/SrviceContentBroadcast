package com.test.ashish.androidpractice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private Boolean isGeneratorOn = false;
    private int maxNo = 50;
    private int minNo = 0;
    private int randomNo;
    public static final String TAG = "MyService";

    public static final int GET_COUNT = 0;

    private class RandomNoRequestHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case GET_COUNT:
                    Message senNo = Message.obtain(null, GET_COUNT);
                    senNo.arg1 = getRandomNo();
                    try {
                        msg.replyTo.send(senNo);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
            }
            super.handleMessage(msg);
        }
    }

    private Messenger randomNoMessenger = new Messenger(new RandomNoRequestHandler());

    @Override
    public IBinder onBind(Intent intent) {
        return randomNoMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomGen();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private int getRandomNo(){
        return randomNo;
    }

    private void stopRandomGen(){
        isGeneratorOn = false;
    }


    private void startRandomGen(){
        while(isGeneratorOn){
            try {
                Thread.sleep(100);
                if(isGeneratorOn) {
                    randomNo = new Random().nextInt(maxNo) + minNo;
                    Log.i(TAG, "Random No : " + String.valueOf(randomNo));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomGen();
    }
}
