package com.anyuaning.osp.ui.fragment.sport;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anyuaning.osp.R;
import com.anyuaning.osp.service.pendometer.StepService;
import com.anyuaning.osp.ui.fragment.OnFragmentInteractionListener;
import com.anyuaning.osp.utils.StringUtils;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CountStepFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Activity                        mActivity;

    private StepService                     mStepService;

    private boolean                         isRunning = false;


    private TextView                        mTvSteps;

    private int                             mStepValue;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountStepFragment.
     */
    public static CountStepFragment newInstance(String param1, String param2) {
        CountStepFragment fragment = new CountStepFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public CountStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count_step, container, false);
        mTvSteps = (TextView) view.findViewById(R.id.tv_step_value);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mActivity = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void startStepService() {
        if (!isRunning) {
            Intent intent = new Intent(mActivity, StepService.class);
//            intent.setAction(OspAction.ACTION_STEP_SERVICE);
            mActivity.startService(intent);
            isRunning = true;
        }
    }

    public void stopStepService() {
        if (null != mStepService) {
            Intent intent = new Intent(mActivity, StepService.class);
//            intent.setAction(OspAction.ACTION_STEP_SERVICE);
            mActivity.stopService(intent);
        }
        isRunning = false;
    }

    public void bindStepService() {
        Intent intent = new Intent(mActivity, StepService.class);
//        intent.setAction(OspAction.ACTION_STEP_SERVICE);
        mActivity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindStepService() {
        mActivity.unbindService(mServiceConnection);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStepService = ((StepService.StepBinder) service).getService();
            mStepService.registerCallback(mCallback);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mStepService = null;
        }
    };

    private StepService.ICallback mCallback = new StepService.ICallback() {
        @Override
        public void stepsChanged(int value) {
            mHandler.obtainMessage(MSG_STEPS, value, 0).sendToTarget();
        }
    };

    private Handler mHandler = new Handler() {
      public void handleMessage(Message msg) {
          switch(msg.what) {
              case MSG_STEPS: {
                  mStepValue = msg.arg1;
                  mTvSteps.setText(StringUtils.toString(mStepValue));
                  break;
              }
              default: {
                  break;
              }
          }
       }
    };

    private static final int MSG_STEPS = 0x0001;
}
