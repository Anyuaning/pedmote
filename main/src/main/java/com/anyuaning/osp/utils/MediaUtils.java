package com.anyuaning.osp.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;

import com.anyuaning.osp.config.OspAction;
import com.anyuaning.osp.service.ServiceToken;
import com.anyuaning.osp.service.music.IMusicPlayerService;

/**
 * Created by thom on 14-4-12.
 */
public class MediaUtils {

    /**
     * query media no limit
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public static Cursor query(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(context, uri, projection, selection, selectionArgs, sortOrder, 0);
    }

    /**
     * query media
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @param limit
     * @return
     */
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

    /**
     * bind music service
     * @param context
     * @param callback
     * @return
     */
    public static ServiceToken bindToMusicService(Activity context, ServiceConnection callback) {
        Activity realActivity = context.getParent();
        if (null == realActivity) {
            realActivity = context;
        }

        ContextWrapper wrapper = new ContextWrapper(realActivity);
        Intent intent = new Intent();
        intent.setAction(OspAction.ACTION_MUSIC_SERVICE);
        wrapper.startService(intent);

        ServiceMusicBinder musicBinder = new ServiceMusicBinder(callback);
        if (wrapper.bindService(intent, musicBinder, 0)) {
            return new ServiceToken(wrapper);
        }

        return null;
    }

    public static IMusicPlayerService sMusicService = null;

    private static class ServiceMusicBinder implements ServiceConnection {

        ServiceConnection mCallback;

        public ServiceMusicBinder(ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            sMusicService = IMusicPlayerService.Stub.asInterface(service);
            if (null != mCallback) {
                mCallback.onServiceConnected(name, service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (null != mCallback) {
                mCallback.onServiceDisconnected(name);
            }
            sMusicService = null;
        }
    }
}
