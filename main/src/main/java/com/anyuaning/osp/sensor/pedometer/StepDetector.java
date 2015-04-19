package com.anyuaning.osp.sensor.pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.anyuaning.osp.service.pendometer.StepListener;

import java.util.ArrayList;

/**
 * Created by thom on 14-4-22.
 */
public class StepDetector implements SensorEventListener {

    private float       mLimit = 10;
    private float       mLastValues[] = new float[3*2];
    private float       mScale[] = new float[2];
    private float       mYOffset;

    private float       mLastDirections[] = new float[3*2];
    private float       mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float       mLastDiff[] = new float[3*2];
    private int         mLastMatch = -1;

    private float[]     mRotation = new float[11];
    /**
     * mOrientation[0]: azimuth, rotation around the z axis
     * mOrientation[1]: pitch, rotation around the x axis
     * mOrientation[2]: roll, rotation around the y axis
     */
    private float[]     mOrientation = new float[3];

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public StepDetector() {
        int h = 540;
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    /**
     * add StepListener
     * @param listener
     */
    public void addStepListener(StepListener listener) {
        mStepListeners.add(listener);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        synchronized (this) {
            mOrientation = SensorManager.getOrientation(mRotation, mOrientation);
            if (false) {

            } else {
                int accelerateType = (sensor.getType() == sensor.TYPE_ACCELEROMETER ? 1 : 0);
                if (accelerateType == 1) {
                    float vSum = 0;
                    for (int i=0; i<3; i++) {
                        final float v = mYOffset + event.values[i] * mScale[accelerateType];
                        vSum += v;
                    }
                    int j = 0;
                    float s = vSum / 3;
                    float direction = (s > mLastValues[j] ? 1 : (s < mLastValues[j] ? - 1 : 0));
                    if (direction == -mLastDirections[j]) {
                        int extType = (direction > 0 ? 0 : 1);
                        mLastExtremes[extType][j] = mLastValues[j];
                        float diff = Math.abs(mLastExtremes[extType][j] - mLastExtremes[1 - extType][j]);
                        if (diff > mLimit) {
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[j] * 2 /3);
                            boolean isPreviousLargeEnough = mLastDiff[j] > (diff / 3);
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                for (StepListener stepListener : mStepListeners) {
                                    stepListener.onStep();;
                                }
                                mLastMatch = extType;
                            } else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[j]  = diff;
                    }

                    mLastDirections[j] = direction;
                    mLastValues[j]  = s;

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
