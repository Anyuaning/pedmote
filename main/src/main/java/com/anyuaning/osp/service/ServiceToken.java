package com.anyuaning.osp.service;

import android.content.ContextWrapper;

/**
 * Created by thom on 14-4-13.
 */
public class ServiceToken {

    ContextWrapper mWrapperContext;

    public ServiceToken(ContextWrapper context) {
        mWrapperContext = context;
    }

}
