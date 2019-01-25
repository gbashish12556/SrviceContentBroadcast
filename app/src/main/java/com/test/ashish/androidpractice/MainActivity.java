package com.test.ashish.androidpractice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = MainActivity.class.getSimpleName();
    Button startServiceButton, stopServiceButton, bindService, unbindService, getRandomNo;
    int count = 0;
    TextView randomNo;
    private MyAsyncTask myAsyncTask;
    private ServiceConnection serviceConnection;
    private TextView randomNoText;

    private boolean isServiceBound, mStopLoop;
    private Intent serviceIntent;
    private MyBoundService myBoundService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this, MyBoundService.class);
        startServiceButton  = findViewById(R.id.startService);
        startServiceButton.setOnClickListener(this);

        stopServiceButton  = findViewById(R.id.stopService);
        stopServiceButton.setOnClickListener(this);

        bindService = findViewById(R.id.bindService);
        bindService.setOnClickListener(this);
        unbindService = findViewById(R.id.unbindService);
        unbindService.setOnClickListener(this);

        randomNoText = findViewById(R.id.randomNo);
        getRandomNo = findViewById(R.id.getRandomNo);
        getRandomNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.startService:
                Log.i(TAG,"Starting Service");
                startService(serviceIntent);
                break;
            case R.id.stopService:
                Log.i(TAG,"Stopping Service");
                stopService(serviceIntent);
                break;
            case R.id.bindService:
                Log.i(TAG,"Binding Service");
                bindService();
                break;
            case R.id.unbindService:
                Log.i(TAG,"Unbindin Service");
                unbindService();
                break;
            case R.id.getRandomNo:
                Log.i(TAG,"Setting Random No");
                setRandomNo();
                break;

        }
    }

    public void bindService(){
        Log.d("serviceConnection", String.valueOf(serviceConnection));
        if(serviceConnection == null){
            serviceConnection = new ServiceConnection(){

                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyBoundService.MyServiceBinder myServiceBinder = (MyBoundService.MyServiceBinder) service;
                    myBoundService = ((MyBoundService.MyServiceBinder) myServiceBinder).getService();
                    Log.d(TAG, "Service Bounded");
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.d(TAG, "Service Failed");
                    isServiceBound = false;
                }

                @Override
                public void onBindingDied(ComponentName name) {
                    Log.d(TAG, "onBindingDied");
                }

                @Override
                public void onNullBinding(ComponentName name) {
                    Log.d(TAG, "onNullBinding");
                }
            };
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void unbindService(){
        Log.d("isServiceBound", String.valueOf(isServiceBound));
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNo(){
        Log.d("isServiceBound", String.valueOf(isServiceBound));
        if(isServiceBound){
            randomNoText.setText("Random No: "+myBoundService.getRandomNo());
        }else{
            randomNoText.setText("Service Not Bound");
        }
    }
    class MyAsyncTask extends AsyncTask<Integer,Integer,Integer>{

        private int customNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customNumber = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            customNumber = integers[0];
            while(mStopLoop){

            }
            return null;
        }
    }
}
