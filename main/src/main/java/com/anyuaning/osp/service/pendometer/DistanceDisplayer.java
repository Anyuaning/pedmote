package com.anyuaning.osp.service.pendometer;

/**
 * Created by thom on 14-4-26.
 */
public class DistanceDisplayer extends StepListener<Float> {

    float mStepLength = 20; // TODO need user setting

    @Override
    public void onStep() {
        mValue  += (float) (mStepLength / 100000.0); // centimeters/kilometer

        notifyDisplayListener();
    }

    @Override
    public void passValue() {

    }

    public void setDistance(float distance) {
        mValue = distance;
    }
}
