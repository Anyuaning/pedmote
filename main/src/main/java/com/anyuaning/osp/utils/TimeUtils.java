package com.anyuaning.osp.utils;

import android.content.Context;

import com.anyuaning.osp.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by thom on 14-4-12.
 */
public class TimeUtils {

    // static member variable
    private static StringBuilder sFormatBuilder = new StringBuilder();

    private static Formatter sFormatter = new Formatter(sFormatBuilder, Locale.getDefault());

    private static final Object[] sfTimeArgs = new Object[5];

    /**
     * format time string
     * @param context
     * @param secs
     * @return
     */
    public static String convertTimeString(Context context, long secs) {
        String durationFormat = context.getString(secs < 3600 ?
                R.string.duration_format_short : R.string.duration_format_long);

        sFormatBuilder.setLength(0);

        final Object[] timeArgs = sfTimeArgs;
        timeArgs[0]  = secs / 3600;
        timeArgs[1] = secs / 60;
        timeArgs[2] = (secs / 60) % 60;
        timeArgs[3] = secs;
        timeArgs[4] = secs % 60;

        return sFormatter.format(durationFormat, timeArgs).toString();
    }

}
