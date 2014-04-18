package com.anyuaning.osp.service.music;

interface IMusicPlayerService {

    void open(in long[] list, int position);

    void openFile(String path);

    void play();

    void playFile(String path);

    boolean isPlaying();

    boolean isPaused();

    void resume();

    void pause();

    void playNext();

    void playPrev();

    void stop();

    int getPlayingPosition();

    int getDuration();

    void setShuffle(boolean shuffle);

    int getAlbumCursorPosition();

    int getSongCursorPosition();

    boolean setAlbumCursorPosition(int position);

    boolean setSongCursorPosition(int position);

    void forward();

    void reverse();

    void seekTo(int msec);

    void setPlayList(long playList);

    void setRecentPeriod(int period);

    void resetAlbumCursor();

    void reloadCursors();

    void destroy();

    void setScrobbleDroid(boolean val);

    
}