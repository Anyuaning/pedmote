package com.anyuaning.osp.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by thom on 14-4-12.
 */
public class MediaUtils {

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }

    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, int limit) {
        Cursor cursor = null;

        ContentResolver resolver = context.getContentResolver();

        if (null != resolver) {
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", "" + limit).build();
            }

            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        }

        return cursor;
    }
}
