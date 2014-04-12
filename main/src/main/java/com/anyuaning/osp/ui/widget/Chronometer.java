package com.anyuaning.osp.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * Created by thom on 14-4-2.
 */
public class Chronometer extends TextView {

    private static final int TICK_WHAT = 2;
    
    private long base;
    
    private boolean isVisible;
    
    private boolean isStarted;
    
    private boolean isRunning;
    
    private onChronometerTickListener onChronometerTickListener;
    
    public Chronometer(Context context) {
        this(context, null);
    }

    public Chronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        initChronometer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        
        isVisible = visibility == VISIBLE;
        updateRunning();
    }

    public void start() {
        isStarted = true;
        updateRunning();
    }

    public void stop() {
        isStarted = false;
        updateRunning();
    }

    private void updateRunning() {
        boolean running = isVisible && isStarted;
        if (running != isRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 10);
            } else {
                mHandler.removeMessages(TICK_WHAT);
            }
            isRunning = running;
        }
        
    }

    private void dispatchChronometerTick() {
        if (null != onChronometerTickListener) {
            onChronometerTickListener.onChrometerTick(this);
        }
    }

    private void initChronometer() {
        base = SystemClock.elapsedRealtime();
        updateText(base);
    }

    private synchronized void updateText(long now) {
        long timeElapsed = now - base;

        DecimalFormat df = new DecimalFormat("00");
        
        int hours = (int) (timeElapsed / (3600 * 1000));
        int remaining = (int) (timeElapsed % (3600 * 1000));
        
        int minutes = remaining / (60 * 1000);
        remaining = remaining % (60 * 1000);
        
        int seconds = remaining / (1000);
        remaining = remaining % (1000);
        
        int millseconds = (((int) timeElapsed % 1000)) / 10;
        
        StringBuilder strBuilder = new StringBuilder();
        if (hours > 0) {
            strBuilder.append(df.format(hours) + ":");
        }
        strBuilder.append(df.format(minutes) + ":");
        strBuilder.append(df.format(seconds) + ":");
        strBuilder.append(millseconds);
        
        setText(strBuilder.toString());
    }

    public void setBase(long base) {
        this.base = base;
        dispatchChronometerTick();
        updateText(SystemClock.elapsedRealtime());
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
        updateRunning();
    }
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (isRunning) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                sendMessageDelayed(Message.obtain(this, TICK_WHAT), 10);
            }
        }
    };
    
    public interface onChronometerTickListener {
        void onChrometerTick(Chronometer chronometer);
    }
}
