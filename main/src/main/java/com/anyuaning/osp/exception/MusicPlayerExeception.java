package com.anyuaning.osp.exception;

/**
 * Created by thom on 14-4-10.
 */
public class MusicPlayerExeception extends RuntimeException {

    public MusicPlayerExeception() {
    }

    public MusicPlayerExeception(String detailMessage) {
        super(detailMessage);
    }

    public MusicPlayerExeception(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public MusicPlayerExeception(Throwable throwable) {
        super(throwable);
    }
}
