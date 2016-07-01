package com.example.liut1.bounce;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

/**
 * Created by liut1 on 6/30/16.
 */
public class BounceDetectManager extends BounceDetect{
    private BounceDetect mBounceDetect;
    private SensorManager mSensorManager;
    public BounceDetectManager(BounceDetectCallback cb, Context context, String service) {
        super(cb);
        mBounceDetect = new BounceDetect(cb);
        mSensorManager = (SensorManager) context.getSystemService(service);
    }
    public BounceDetect bounceDetectGet(){
        return mBounceDetect;
    }
    public void BounceSensorRegistor(){
        mSensorManager.registerListener(mBounceDetect,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    public void BounceSensorUnregister(){
        mSensorManager.unregisterListener(mBounceDetect);
    }
}
