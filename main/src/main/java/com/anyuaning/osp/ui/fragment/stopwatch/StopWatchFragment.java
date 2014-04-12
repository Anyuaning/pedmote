package com.anyuaning.osp.ui.fragment.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.anyuaning.osp.R;

/**
 * Created by thom on 14-4-2.
 */
public class StopWatchFragment extends Fragment {

    private boolean isRunning = false;

    private Button mBtnStart;

    private Button mBtnReset;

    private Chronometer mChronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        mBtnStart = (Button) view.findViewById(R.id.btn_start_stop);
        mBtnReset = (Button) view.findViewById(R.id.btn_reset);
        mChronometer = (Chronometer) view.findViewById(R.id.chronometer);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    mChronometer.stop();
                    isRunning = false;
                    mBtnStart.setText(R.string.btn_start);
                } else {
//                    mChronometer.setBase(SystemClock.elapsedRealtime()); // stop to pause
                    mChronometer.start();
                    isRunning = true;
                    mBtnStart.setText(R.string.btn_stop);
                }
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isRunning) {
                    mChronometer.stop();
                }
                mChronometer.setBase(SystemClock.elapsedRealtime());
                if (isRunning) {
                    mChronometer.start();
                }
            }
        });

        return view;
    }

}
