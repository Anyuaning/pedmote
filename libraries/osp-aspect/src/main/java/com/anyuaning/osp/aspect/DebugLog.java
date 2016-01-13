package com.anyuaning.osp.aspect;

import android.util.Log;

/**
 * Created by thom on 1/13/16.
 */

public class DebugLog {

    private DebugLog() {}

    /**
     * Send a debug log message
     *
     * @param tag Source of a log message. It usually identifies the class or activity where the log
     * call occurs.
     * @param message The message you would like logged.
     */
    public static void log(String tag, String message) {
        Log.d(tag, message);
    }
}
