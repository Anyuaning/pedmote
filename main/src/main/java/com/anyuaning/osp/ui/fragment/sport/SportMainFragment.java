package com.anyuaning.osp.ui.fragment.sport;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anyuaning.osp.R;

/**
 * Created by thom on 14-4-21.
 */
public class SportMainFragment extends Fragment {

    private Activity    mActivity;

    private Button      mBtnStart;

    private Button      mBtnSave;

    private Button      mBtnReset;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sport_main, container, false);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        final StopWatchFragment stopWatchFragment = new StopWatchFragment();
        fragmentTransaction.add(R.id.fragment_timing, stopWatchFragment);

        final CountStepFragment countStepFragment = new CountStepFragment();
        fragmentTransaction.add(R.id.fragment_count_step, countStepFragment);

        fragmentTransaction.commit();

        mBtnStart = (Button) view.findViewById(R.id.btn_start_stop);
        mBtnSave = (Button) view.findViewById(R.id.btn_save);
        mBtnReset = (Button) view.findViewById(R.id.btn_reset);

        mBtnSave.setVisibility(View.GONE);

        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopWatchFragment.isRunning()) {
                    mBtnStart.setText(R.string.label_btn_start);
                    mBtnSave.setVisibility(View.VISIBLE);

                    countStepFragment.stopStepService();
                    countStepFragment.unbindStepService();
                } else {
                    mBtnStart.setText(R.string.label_btn_pause);
                    mBtnSave.setVisibility(View.GONE);

                    countStepFragment.startStepService();
                    countStepFragment.bindStepService();
                }
                stopWatchFragment.startOrPauseTiming();


            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopWatchFragment.saveTiming();
                stopWatchFragment.resetTiming();
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mBtnSave.setVisibility(View.GONE);
                stopWatchFragment.resetTiming();
                stopWatchFragment.setReset(true);
            }
        });

        return view;
    }
}
