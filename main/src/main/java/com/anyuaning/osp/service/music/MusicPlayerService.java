package com.anyuaning.osp.service.music;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.anyuaning.osp.R;
import com.anyuaning.osp.exception.MusicPlayerExeception;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by thom on 14-4-10.
 */
public class MusicPlayerService extends Service {

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

    private boolean isPaused;

    private void play(int albumCursorPosition, int songCursorPosition) {

    }

    /**
     * play song
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
        public void play(int albumCursorPosition, int songCursorPosition) throws RemoteException {
            weakReference.get().play(albumCursorPosition, songCursorPosition);
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
}
