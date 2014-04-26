package com.anyuaning.osp.sensor.pedometer;

/**
 * Created by thom on 14-4-22.
 */
public interface StepListener {

    /**
     * count step
     */
    public void onStep();

    /**
     *
     */
    public void passValue();

}
