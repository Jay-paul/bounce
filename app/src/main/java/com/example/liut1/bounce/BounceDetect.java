package com.example.liut1.bounce;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by liut1 on 6/30/16.
 */
public class BounceDetect implements SensorEventListener {
    public interface BounceDetectCallback{
        public void doSuccess(float x, float y, float z);
        public void doFailed();
    }
    private BounceDetectCallback bcb;
    public BounceDetect(BounceDetectCallback cb){
        bcb = cb;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        switch (sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                accelerCallback(event.values[0],event.values[1],event.values[2]);
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void accelerCallback(float x, float y, float z){
        bcb.doSuccess(x, y, z);
    }
}
