package com.anyuaning.osp.service.music;

interface IMusicPlayerService {

    /**
     * play music
     * @param albumCursorPosition
     * @param songCursorPosition
     **/
    void play(int albumCursorPosition, int songCursorPosition);

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