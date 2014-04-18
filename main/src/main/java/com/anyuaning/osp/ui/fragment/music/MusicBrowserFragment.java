package com.anyuaning.osp.ui.fragment.music;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anyuaning.osp.R;
import com.anyuaning.osp.service.ServiceToken;
import com.anyuaning.osp.service.music.IMusicPlayerService;
import com.anyuaning.osp.utils.MediaUtils;
import com.anyuaning.osp.utils.TimeUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by thom on 14-4-12.
 */
public class MusicBrowserFragment extends Fragment implements ServiceConnection {

    private Activity mContext;

    private PullToRefreshListView mPullRefreshMusicList;

    private boolean isNowPlaying;

    private ServiceToken mMusicServiceToken;

    private IMusicPlayerService mMusicService;

    @Override
    public void onAttach(Activity activity) {
        mContext = activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMusicServiceToken = MediaUtils.bindToMusicService(mContext, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        mPullRefreshMusicList = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_music_list);

        Cursor cursor = MediaUtils.query(mContext, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        MusicCursorAdapter adapter = new MusicCursorAdapter(mContext, cursor, false);
        mPullRefreshMusicList.setAdapter(adapter);

        mPullRefreshMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Adapter adapter = null;
                if (parent.getAdapter() instanceof HeaderViewListAdapter) {
                    HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) parent.getAdapter();
                    adapter = listAdapter.getWrappedAdapter();
                } else {
                    adapter = parent.getAdapter();
                }
                if (adapter instanceof  MusicCursorAdapter) {
                    MusicCursorAdapter cursorAdapter = (MusicCursorAdapter) adapter;
                    Cursor cursor = cursorAdapter.getCursor();
                    String songPath = cursor.getString(cursor.getColumnIndexOrThrow((MediaStore.Audio.Media.DATA)));
                    Toast.makeText(mContext, "pos: " + songPath, Toast.LENGTH_SHORT).show();
                    try {
//                        mMusicService.playFile(songPath);
//                        mMusicService.openFile(songPath);
                        long[] list = getSongListForCursor(cursor);
                        mMusicService.open(list, position);
                        mMusicService.play();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        return view;
    }



    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMusicService = IMusicPlayerService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
    private long[] getSongListForCursor(Cursor cursor) {
        if (null == cursor) {
            return new long[]{};
        }

        int len = cursor.getCount();
        long[] list = new long[len];
        cursor.moveToFirst();
        int colidx = -1;
        try {
            colidx = cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.Members.AUDIO_ID);
        } catch (IllegalArgumentException e) {
            colidx = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        }
        for (int i=0; i<len; i++) {
            list[i] = cursor.getLong(colidx);
            cursor.moveToNext();
        }

        return list;
    }

    class MusicCursorAdapter extends CursorAdapter {

        public MusicCursorAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.list_music_info, null);
            holder.imageMusicIcon = (ImageView) view.findViewById(R.id.image_music_icon);
            holder.textMusicLine1 = (TextView) view.findViewById(R.id.text_music_line1);
            holder.textMusicLine2 = (TextView) view.findViewById(R.id.text_music_line2);
            holder.textMusicDuration = (TextView) view.findViewById(R.id.text_music_duration);
            holder.imageMusicIndicator = (ImageView) view.findViewById(R.id.image_music_indicator);
            holder.buffer1 = new CharArrayBuffer(100);
            holder.buffer2 = new char[200];
            view.setTag(holder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder holder = (ViewHolder) view.getTag();
            int titleIdx = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIdx = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationIdx = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            // music title
            cursor.copyStringToBuffer(titleIdx, holder.buffer1);
            holder.textMusicLine1.setText(holder.buffer1.data, 0, holder.buffer1.sizeCopied);

            // music artist
            String name = cursor.getString(artistIdx);
            StringBuilder infoBuilder = new StringBuilder();
            if (null == name || name.equals(MediaStore.UNKNOWN_STRING)) {
                infoBuilder.append(mContext.getString(R.string.music_unkown_artist));
            } else {
                infoBuilder.append(name);
            }

            int len = infoBuilder.length();
            if (holder.buffer2.length < len) {
                holder.buffer2 = new char[len];
            }
            infoBuilder.getChars(0, len, holder.buffer2, 0);
            holder.textMusicLine2.setText(holder.buffer2, 0, len);

            // music duration
            int secs = cursor.getInt(durationIdx) / 1000;
            if (secs == 0) {
                holder.textMusicDuration.setText("");
            } else {
                holder.textMusicDuration.setText(TimeUtils.convertTimeString(mContext, secs));
            }

            // music indicator
            ImageView iv = holder.imageMusicIndicator;
            long id = -1;
            if ((isNowPlaying && cursor.getPosition() == id)
                    || (!isNowPlaying)) {
                iv.setVisibility(View.VISIBLE);
            } else {
                iv.setVisibility(View.GONE);
            }

        }

        final class ViewHolder {
            ImageView imageMusicIcon;
            TextView textMusicLine1;
            TextView textMusicLine2;
            TextView textMusicDuration;
            ImageView imageMusicIndicator;

            CharArrayBuffer buffer1;
            char[] buffer2;

        }
    }
}
