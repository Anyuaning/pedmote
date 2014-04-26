package com.anyuaning.osp.sensor.pedometer;

import java.util.ArrayList;

/**
 * Created by thom on 14-4-23.
 */
public class StepDisplayer implements StepListener {

    private int mCount = 0;

    private ArrayList<DisplayListener> mDisplayListeners = new ArrayList<DisplayListener>();

    @Override
    public void onStep() {
        mCount ++;
        notifyDisplayListener();
    }

    @Override
    public void passValue() {

    }

    public void setSteps(int steps) {
        mCount = steps;
        notifyDisplayListener();
    }

    public void addDisplayListener(DisplayListener listener) {
        mDisplayListeners.add(listener);
    }

    public void notifyDisplayListener() {
        for (DisplayListener listener : mDisplayListeners) {
            listener.stepsChanged(mCount);
        }
    }

    public interface DisplayListener {
        public void stepsChanged(int value);
        public void passValue();
    }
}
