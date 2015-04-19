package com.anyuaning.osp.service.pendometer;

/**
 * Created by thom on 14-4-26.
 */
public class CaloriesDisplayer extends StepListener<Double> {

    private static double METRIC_RUNNING_FACTOR = 1.0274823;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERTIAL_WALKING_FACTOR = 0.517;

    float mStepLength = 20;
    float mBodyWeight = 127.75f; // puond (1p = 0.454k)

    boolean isRunning = true;

    @Override
    public void onStep() {
        mValue += (mBodyWeight * (isRunning ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR)
                * mStepLength / 100000.0) ;

        notifyDisplayListener();
     }

    @Override
    public void passValue() {

    }

    public void setCalories(double calories) {
        mValue = calories;
        notifyDisplayListener();
    }
}
