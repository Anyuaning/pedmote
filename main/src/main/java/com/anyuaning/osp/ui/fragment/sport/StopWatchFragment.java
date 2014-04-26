package com.anyuaning.osp.ui.fragment.sport;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.anyuaning.osp.OspApplication;
import com.anyuaning.osp.R;
import com.anyuaning.osp.dao.DaoSession;
import com.anyuaning.osp.dao.sport.TimingDao;
import com.anyuaning.osp.model.sport.Timing;
import com.anyuaning.osp.utils.StringUtils;
import com.anyuaning.osp.utils.TimeUtils;

import java.util.Date;

/**
 * Created by thom on 14-4-2.
 */
public class StopWatchFragment extends Fragment {



    private boolean isRunning = false;


    private Chronometer mChronometer;

    private Activity mContext;



    private boolean isReset = true;
    private boolean isPaused = false;
    private Date startTime;
    private Date endTime;

    private long pauseCurTime;
    private long alreadyTime;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);


        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);


        return view;
    }

    /**
     * start or pause timing
     */
    public void startOrPauseTiming() {
        if (isRunning) {
            mChronometer.stop();
            isRunning = false;

            endTime = new Date();
            alreadyTime = TimeUtils.convertTimeLong(mContext,
                    StringUtils.toString(mChronometer.getText())) * 1000;
            isPaused = true;
        } else {

            mChronometer.setBase(SystemClock.elapsedRealtime() - alreadyTime); // Chronometer init set mBase

            mChronometer.start();
            isRunning = true;

            if (isReset) {
                startTime = new Date();
                isReset = false;
            }
        }
    }

    /**
     * save timing
     */
    public void saveTiming() {
        OspApplication ospApp = (OspApplication) mContext.getApplication();
        DaoSession daoSession = ospApp.getDaoSession();
        TimingDao timingDao = daoSession.getTimingDao();
        Timing timing = new Timing();
        timing.setStartTime(startTime);
        timing.setStartFormatterTime(StringUtils.formatDate(startTime));
        timing.setEndTime(endTime);
        timing.setEndFormatterTime(StringUtils.formatDate(endTime));
        String timingStr = StringUtils.toString(mChronometer.getText());
        long timingLen = TimeUtils.convertTimeLong(mContext, timingStr);
        timing.setLength(timingLen);
        timingDao.insert(timing);
    }

    /**
     * reset timing
     */
    public void resetTiming() {

        if (isRunning) {
            mChronometer.stop();
        }
        mChronometer.setBase(SystemClock.elapsedRealtime());
        if (isRunning) {
            mChronometer.start();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean isReset) {
        this.isReset = isReset;
    }
}
