package com.anyuaning.osp.service.music;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.MediaStore;

import com.anyuaning.osp.R;
import com.anyuaning.osp.broadcast.MediaButtonReceiver;
import com.anyuaning.osp.exception.MusicPlayerExeception;
import com.anyuaning.osp.utils.MediaUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.Vector;

/**
 * Created by thom on 14-4-10.
 */
public class MusicPlayerService extends Service {

    private static final int MUSIC_WENT_TO_NEXT = 0x1000;
    private static final int FOCUS_CHANGE = 0x1001;

    private static final int MAX_HISTORY_SIZE = 100;


    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }

    /**
     * song cursor
     */
    private Cursor mSongCursor;

    /**
     * album cursor
     */
    private Cursor mAlbumCursor;

    /**
     * media player
     */
    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private MultiMusicPlayer mMultiPlayer;

    private ComponentName mComponentMedia;

    private boolean isPaused;

    private int mPlayPos = -1;
    private int mNextPlayPos = -1;
    private long [] mPlayList = null;
    private int mPlayListLen = 0;
    private String mFileToPlay;

    private int mOpenFailedCounter = 0;

    private final Shuffler mRandShuffer = new Shuffler();

    private Vector<Integer> mHistory = new Vector<Integer>(MAX_HISTORY_SIZE);

    private String[] mCursorCols = new String[] {
        "audio._id AS _id",
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.MIME_TYPE,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.IS_PODCAST,
        MediaStore.Audio.Media.BOOKMARK
    };

    private Handler mMusicPlayerHandler = new Handler() {
        float currentVolume = 1.0f;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MUSIC_WENT_TO_NEXT: {
                    next();
                    break;
                }
            }
        }
    };

    /**
     * set the next song info
     */
    private void next() {
        mPlayPos = getNextPosition(false);
        openCurrent();
        play();
    }

    /**
     * obtian next song position
     * TODO: repeat mode is different
     * @param b
     * @return
     */
    private int getNextPosition(boolean b) {
        if (mPlayPos >= 0) {
            mHistory.add(mPlayPos);
        }
        if (mHistory.size() > MAX_HISTORY_SIZE) {
            mHistory.removeElementAt(0);
        }
        int numMusics = mPlayListLen;
        int[] musics = new int[numMusics];
        for (int i=0; i<numMusics; i++) {
            musics[i] = i;
        }

        int numHistory = mHistory.size();
        int numUnplayed = numMusics;
        for (int i=0; i<numHistory; i++) {
            int idx = mHistory.get(i).intValue();
            if (idx < numMusics && musics[idx] >= 0) {
                numUnplayed --;
                musics[idx] = -1;
            }
        }

        if (numUnplayed <= 0) {
            if (false) {
                // pick from full set
            } else {
                return -1;
            }
        }

        int skip = mRandShuffer.nextInt(numUnplayed);
        int cnt = -1;
        while (true) {
            while (musics[++cnt] < 0) {

            }
            skip --;
            if (skip < 0) {
                break;
            }
        }
        return cnt;
    }

    /**
     * get the song cursor by _id
     * @param lid
     * @return
     */
    private Cursor getSongCursorForId(long lid) {
        String id = String.valueOf(lid);
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = mCursorCols;
        String selection = "_id = ?";
        String[] selectionArgs = new String[] {
                id
        };
        String sortOrder = null;
        Cursor cursor = MediaUtils.query(this, uri, projection, selection, selectionArgs, sortOrder);
        if (null != cursor) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public static final String PLAYER_ACTION = "com.anyuaning.osp.action.MUSIC_SERVICE";

    public static final String PLAYER_CMDNAME = "cmdname";

    public static final String PLAYER_CMDNEXT = "cmdnext";

    private String mCmdName  = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mCmdName = intent.getStringExtra(PLAYER_CMDNAME);
        if (PLAYER_CMDNEXT.equals(mCmdName)) {
            next();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * get the song cursor by data
     * @param data
     * @return
     */
    private Cursor getSongCursorForData(String data) {
        Uri uri = null;
        String[] projection = mCursorCols;
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        if (data.startsWith("content://media/")) {
            uri = Uri.parse(data);
            selection = null;
            selectionArgs = null;
        } else {
            uri = MediaStore.Audio.Media.getContentUriForPath(data);
            selection = MediaStore.Audio.Media.DATA + " = ?";
            selectionArgs = new String[] {data};
        }

        Cursor cursor = MediaUtils.query(this, uri, projection, selection, selectionArgs, sortOrder);

        return cursor;
    }

    private boolean open(String path) {
        synchronized (this) {
            if (null == path) {
                return false;
            }
            mSongCursor = getSongCursorForData(path);
            if (null != mSongCursor) {
                if (mSongCursor.getCount() == 0) {
                    mSongCursor.close();
                    mSongCursor = null;
                } else {
                    mSongCursor.moveToNext();
                    ensurePlayListCapacity(1);
//                    mPlayListLen = 1;
//                    mPlayList[0] = mSongCursor.getLong(0);
//                    mPlayPos = 0;
                }
            }

            mFileToPlay = path;
            mMultiPlayer.setDataSource(mFileToPlay);
            if (mMultiPlayer.isInitialized()) {
                mOpenFailedCounter = 0;
                return true;
            }
            stop(true);
            return false;
        }
    }

    private void open(long[] list, int position) {
        synchronized (this) {

            int listLength = list.length;
            boolean newList = true;
            if (mPlayListLen == listLength) {
                newList = false;
                for (int i = 0; i < listLength; i++) {
                    if (list[i] != mPlayList[i]) {
                        newList = true;
                        break;
                    }
                }
            }

            if (newList) {
                addToPlayList(list, -1);
            }

            if (position >= 0) {
                mPlayPos = position;
            } else {
                mPlayPos = mRandShuffer.nextInt(mPlayListLen);
            }
            mHistory.clear();

            openCurrent();
        }
    }

    private void play() {
        mAudioManager.requestAudioFocus(mAudioFocusListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        if (mMultiPlayer.isInitialized) {
            mMultiPlayer.start();
        }
    }

    /**
     * play song by path
     * @param songPath
     */
    private void playSong(String songPath) {
        Message message = new Message();
        message.obj = songPath;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(songPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MusicPlayerExeception(getString(R.string.exeception_mediaplayer_datasource));
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            throw new MusicPlayerExeception(getString(R.string.exeception_mediaplayer_prepare));
        }
        mMediaPlayer.start();

        isPaused = false;
    }

    private void stop(boolean b) {
        if (mMultiPlayer.isInitialized()) {
            mMultiPlayer.stop();
        }
        mFileToPlay = null;
        if (null != mSongCursor) {
            mSongCursor.close();
            mSongCursor = null;
        }
    }


    private void ensurePlayListCapacity(int size) {
        if (null == mPlayList || size > mPlayList.length) {
            long[] newList = new long[size * 2];
            int len = mPlayList != null ? mPlayList.length : mPlayListLen;
            for (int i=0; i<len; i++) {
                newList[i] = mPlayList[i];
            }
            mPlayList = newList;
        }
    }

    private void addToPlayList(long[] list, int position) {
        int addLen = list.length;
        if (position < 0) {
            mPlayListLen = 0;
            position = 0;
        }

        ensurePlayListCapacity(mPlayListLen + addLen);

        if (position > mPlayListLen) {
            position = mPlayListLen;
        }

        int tailSize = mPlayListLen - position;
        for (int i=tailSize; i>0; i--) {
            mPlayList[position + 1] = mPlayList[position + i - addLen];
        }

        for (int i=0; i<addLen; i++) {
            mPlayList[position + i] = list[i];
        }

        mPlayListLen += addLen;

        if (mPlayListLen == 0) {
            mSongCursor.close();
            mSongCursor = null;

        }
    }

    private void openCurrentAndNext() {

        long id = mPlayList[mPlayPos];
        mSongCursor = getSongCursorForId(id);

        if (null != mSongCursor) {
            mSongCursor.moveToFirst();
            open(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" +id);
        }

        //        setNextMusic();  // api level
    }

    private void openCurrent() {
        synchronized (this) {
            if (mSongCursor != null) {
                mSongCursor.close();
                mSongCursor = null;
            }

            if (mPlayListLen == 0) {
                return;
            }
            stop(false);

            long id = mPlayList[mPlayPos];

            mSongCursor = getSongCursorForId(id);
            if (mSongCursor != null) {
                open(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + id);
            }
        }
    }

    private long getAudioId() {
        synchronized (this) {
            if (mPlayPos >= 0 && mMultiPlayer.isInitialized()) {
                return mPlayList[mPlayPos];
            }
        }

        return -1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioManager.unregisterMediaButtonEventReceiver(mComponentMedia);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mComponentMedia = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(mComponentMedia);

        mMediaPlayer = new MediaPlayer();

        mMultiPlayer = new MultiMusicPlayer();
    }

    private AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            mMusicPlayerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };

    private class MultiMusicPlayer {

        private MediaPlayer mCurrentMusicPlayer = new MediaPlayer();

        private Handler mHandler;

        private boolean isInitialized = false;

        public MultiMusicPlayer() {
            mCurrentMusicPlayer.setWakeMode(MusicPlayerService.this, PowerManager.PARTIAL_WAKE_LOCK);
        }

        public void setDataSource(String path) {
            isInitialized = setDataSourceImpl(mCurrentMusicPlayer, path);
        }

        private boolean setDataSourceImpl(MediaPlayer player, String path) {
            try {
                player.reset();
                player.setOnPreparedListener(null);
                if (path.startsWith("content://")) {
                    player.setDataSource(MusicPlayerService.this, Uri.parse(path));
                } else {
                    player.setDataSource(path);
                }
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            player.setOnCompletionListener(completionListener);
            player.setOnErrorListener(errorListener);

            Intent intent = new Intent(AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION);
            intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, getAudioSessionId());
            intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
            sendBroadcast(intent);
            return true;
        }

        private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mMusicPlayerHandler.sendEmptyMessage(MUSIC_WENT_TO_NEXT);
            }
        };

        private MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        };

        public void start() {
            mCurrentMusicPlayer.start();
        }

        public void stop() {
            mCurrentMusicPlayer.reset();
            isInitialized = false;
        }

        public void release() {
            stop();
            mCurrentMusicPlayer.release();
        }

        public void pause() {
            mCurrentMusicPlayer.pause();
        }

        public long duration() {
            return mCurrentMusicPlayer.getDuration();
        }

        public long position() {
            return mCurrentMusicPlayer.getCurrentPosition();
        }

        public int seek(int whereto) {
            mCurrentMusicPlayer.seekTo(whereto);
            return whereto;
        }

        public void setVolumen(float vol) {
            mCurrentMusicPlayer.setVolume(vol, vol);
        }

        public void setHandler(Handler handler) {
            mHandler = handler;
        }

        public boolean isInitialized() {
            return isInitialized;
        }

        public int getAudioSessionId() {
            return mCurrentMusicPlayer.getAudioSessionId();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceStub(this);
    }

    private class ServiceStub extends IMusicPlayerService.Stub {

        WeakReference<MusicPlayerService> weakReference;

        public ServiceStub(MusicPlayerService service) {
            weakReference = new WeakReference<MusicPlayerService>(service);
        }

        @Override
        public void open(long[] list, int position) throws RemoteException {
            weakReference.get().open(list, position);
        }

        @Override
        public void openFile(String path) throws RemoteException {
            weakReference.get().open(path);
        }

        @Override
        public void play() throws RemoteException {
            weakReference.get().play();
        }

        @Override
        public void playFile(String path) throws RemoteException {
            weakReference.get().playSong(path);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return false;
        }

        @Override
        public boolean isPaused() throws RemoteException {
            return false;
        }

        @Override
        public void resume() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void playNext() throws RemoteException {
            weakReference.get().next();
        }

        @Override
        public void playPrev() throws RemoteException {

        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public int getPlayingPosition() throws RemoteException {
            return 0;
        }

        @Override
        public int getDuration() throws RemoteException {
            return 0;
        }

        @Override
        public void setShuffle(boolean shuffle) throws RemoteException {

        }

        @Override
        public int getAlbumCursorPosition() throws RemoteException {
            return 0;
        }

        @Override
        public int getSongCursorPosition() throws RemoteException {
            return 0;
        }

        @Override
        public boolean setAlbumCursorPosition(int position) throws RemoteException {
            return false;
        }

        @Override
        public boolean setSongCursorPosition(int position) throws RemoteException {
            return false;
        }

        @Override
        public void forward() throws RemoteException {

        }

        @Override
        public void reverse() throws RemoteException {

        }

        @Override
        public void seekTo(int msec) throws RemoteException {

        }

        @Override
        public void setPlayList(long playList) throws RemoteException {

        }

        @Override
        public void setRecentPeriod(int period) throws RemoteException {

        }

        @Override
        public void resetAlbumCursor() throws RemoteException {

        }

        @Override
        public void reloadCursors() throws RemoteException {

        }

        @Override
        public void destroy() throws RemoteException {

        }

        @Override
        public void setScrobbleDroid(boolean val) throws RemoteException {

        }
    };

    private static class Shuffler {
        private int mPrevious;

        private Random mRandom = new Random();

        public int nextInt(int interval) {
            int ret;
            do {
                ret = mRandom.nextInt(interval);
            } while (ret == mPrevious && interval > 1);
            mPrevious = ret;
            return ret;
        }
    }
}
