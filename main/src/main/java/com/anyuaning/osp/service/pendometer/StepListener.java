package com.anyuaning.osp.service.pendometer;

import java.util.ArrayList;

/**
 * Created by thom on 14-4-22.
 */
public abstract class StepListener<T> {

    protected T mValue;

    private ArrayList<DisplayListener> mDisplayListeners = new ArrayList<DisplayListener>();

    public void addDisplayListener(DisplayListener listener) {
        mDisplayListeners.add(listener);
    }

    public void notifyDisplayListener() {
        for (DisplayListener listener : mDisplayListeners) {
            listener.stepsChanged(mValue);
        }
    }

    /***
     * interface in abstract
     */
    public interface DisplayListener<T> {
        public void stepsChanged(T value);
        public void passValue();
    }

    /**
     * count step
     */
    public abstract void onStep();

    /**
     *
     */
    public abstract void passValue();
}
