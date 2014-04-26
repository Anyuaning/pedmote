package com.anyuaning.osp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thom on 14-4-19.
 */
public class StringUtils {

    private StringUtils() {}

    private static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * to string
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return object == null ? "" : String.valueOf(object);
    }

    /**
     * format date to "yyyy-MM-dd HH:mm:ss"
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
