package com.anyuaning.osp.service.pendometer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

import com.anyuaning.osp.sensor.pedometer.StepDetector;
import com.anyuaning.osp.sensor.pedometer.StepDisplayer;

/**
 * Created by thom on 14-4-23.
 */
public class StepService extends Service {

    private StepDetector    mStepDetector;

    private SensorManager   mSensorManager;
    private Sensor          mSensor;

    private StepDisplayer   mStepDisplayer;

    private int             mSteps;

    private ICallback       mCallback;

    private final IBinder   mBinder = new StepBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mStepDetector = new StepDetector();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        registerDetector();

        mStepDisplayer = new StepDisplayer();
        mStepDisplayer.setSteps(0); // TODO save value
        mStepDisplayer.addDisplayListener(mStepDisplayListener);
        mStepDetector.addStepListener(mStepDisplayer);


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void registerDetector() {
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mStepDetector, mSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void registerCallback(ICallback cb) {
        mCallback = cb;
    }

    private StepDisplayer.DisplayListener mStepDisplayListener = new StepDisplayer.DisplayListener() {
        @Override
        public void stepsChanged(int value) {
            mSteps = value;
            passValue();
        }

        @Override
        public void passValue() {
            if (null != mCallback) {
                mCallback.stepsChanged(mSteps);
            }
        }
    };

    public interface ICallback {
        public void stepsChanged(int value);
    }

    public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }
}
