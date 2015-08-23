package com.anyuaning.osp.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.anyuaning.osp.service.music.IMusicPlayerService;
import com.anyuaning.osp.service.music.MusicPlayerService;

/**
 * Created by thom on 8/23/15.
 */
public class MediaButtonReceiver extends BroadcastReceiver {

    private static final String TAG = "MediaButtonReceiver";


    private static final int INTERVAL_TWICE = 2000;

    static long firstEventTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {
            KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (null == keyEvent) {
                return ;
            }

            int eventCode = keyEvent.getKeyCode();
            long eventTime = keyEvent.getEventTime();
            int eventAction = keyEvent.getAction();

            Intent playerIntent = new Intent(context, MusicPlayerService.class);
            playerIntent.setAction(MusicPlayerService.PLAYER_ACTION);
            String command = null;

            switch (eventCode) {
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE: {

                    if (eventAction == KeyEvent.ACTION_DOWN)
                        if (firstEventTime != 0 && (eventTime - firstEventTime) < INTERVAL_TWICE) {
                            // 双击时，无法跳过第一次的执行。只能让第二次的动作覆盖第一次。
                            Toast.makeText(context, "action down twice ", Toast.LENGTH_SHORT).show();
                            command = MusicPlayerService.PLAYER_CMDNEXT;
                            firstEventTime = 0;
                        } else {
                            Log.i(TAG, "action down first 1 " + eventTime);
                            Log.i(TAG, "action down first 0 " + firstEventTime);
                            firstEventTime = eventTime;
                        }
                    if (eventAction == KeyEvent.ACTION_MULTIPLE) {
                        Toast.makeText(context, "action multiple", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default: {
                    break;
                }
            }

            playerIntent.putExtra(MusicPlayerService.PLAYER_CMDNAME, command);
            context.startService(playerIntent);

        }
    }

}
