package com.anyuaning.osp.service.pendometer;

/**
 * Created by thom on 14-4-23.
 */
public class StepDisplayer extends StepListener<Integer> {

    @Override
    public void onStep() {
        mValue++;
        notifyDisplayListener();
    }

    @Override
    public void passValue() {

    }

    public void setSteps(int steps) {
        mValue = steps;
        notifyDisplayListener();
    }

}
