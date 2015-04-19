package com.anyuaning.osp.service.pendometer;

import android.os.SystemClock;

/**
 * Created by thom on 14-4-26.
 */
public class PaceDisplayer extends StepListener<Integer> {

    private int         mCounter;

    private long        mLastStepTime;
    private long[]      mLastStepDeltas = {-1, -1, -1, -1};
    private int         mLastStepDeltasIndex;

    @Override
    public void onStep() {
        long thisStepTime = SystemClock.elapsedRealtime();
        mCounter ++;


        if (mLastStepTime > 0) {
            long delta = thisStepTime - mLastStepTime;

            mLastStepDeltas[mLastStepDeltasIndex] = delta;
            mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;

            int sum = 0;
            boolean isMeaningfull = true;
            for (int i=0; i<mLastStepDeltas.length; i++) {
                if (mLastStepDeltas[i] < 0) {
                    isMeaningfull = false;
                    break;
                }
                sum += mLastStepDeltas[i];
            }

            if (isMeaningfull && sum > 0) {
                int avg = sum / mLastStepDeltas.length;
                mValue = 60 * 1000 / avg;
            }

        } else {
            mValue = -1;
        }

        mLastStepTime = thisStepTime;
        notifyDisplayListener();
    }

    @Override
    public void passValue() {

    }

    /**
     *
     * @param pace
     */
    public void setPace(int pace) {
        mValue = pace;
    }
}
