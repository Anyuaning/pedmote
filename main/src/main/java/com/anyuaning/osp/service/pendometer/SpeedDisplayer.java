package com.anyuaning.osp.service.pendometer;

/**
 * Created by thom on 14-4-26.
 */
public class SpeedDisplayer extends StepListener<Double> implements StepListener.DisplayListener<Double> {

    @Override
    public void onStep() {

    }

    @Override
    public void stepsChanged(Double value) {

    }

    @Override
    public void passValue() {

    }

    public void setSpeed(double speed) {
        mValue = speed;
    }
}
