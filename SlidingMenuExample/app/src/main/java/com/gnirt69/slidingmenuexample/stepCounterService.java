package com.gnirt69.slidingmenuexample;

import android.app.Fragment;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

import java.util.Calendar;

public class stepCounterService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    Intent intent;
    int day;

    public stepCounterService() {
    }
    public void onCreate(){

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(this, mStepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        this.intent = intent;
        Calendar rightNow = Calendar.getInstance();
        day = rightNow.get(Calendar.DAY_OF_WEEK);
        return START_NOT_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        System.out.println("gick in i sensor");
        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            System.out.println("Step Counter Detected : " + value);
            Intent intentReturn = new Intent(MainActivity.RECEIVE_STEPS);
            intentReturn.putExtra("step",value);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentReturn);

        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            System.out.println("Step Detector Detected : " + value);
        }
        if(!(day == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
            onDestroy();
            resume();

        }
        Calendar rightNow = Calendar.getInstance();
        day = rightNow.get(Calendar.DAY_OF_WEEK);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void onDestroy() {
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }
    public void resume(){
        mSensorManager.registerListener(this, mStepCounterSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
