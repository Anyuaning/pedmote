package com.anyuaning.osp.jnimp3lame;

/**
 * Created by thom on 14-5-10.
 */
public class TestNativeMain {

    private native void msg(String str);

    static {
        System.loadLibrary("jnimp3lame");
    }

    public void printMsg(String str) {
        msg(str);
    }
}
