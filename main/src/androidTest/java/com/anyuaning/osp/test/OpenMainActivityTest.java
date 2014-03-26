package com.anyuaning.osp.test;

import android.test.ActivityInstrumentationTestCase2;

import com.anyuaning.osp.R;
import com.anyuaning.osp.ui.home.OpenMainActivity;
import com.handmark.pulltorefresh.library.extras.viewpager.PullToRefreshViewPager;

import static android.test.ViewAsserts.assertOnScreen;

/**
 * Created by thom on 14-3-23.
 */
public class OpenMainActivityTest extends ActivityInstrumentationTestCase2<OpenMainActivity> {

    private OpenMainActivity mOpenMainActivity;

    private PullToRefreshViewPager mPullViewPager;

    public OpenMainActivityTest() {
        super(OpenMainActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        mOpenMainActivity = getActivity();
        mPullViewPager = (PullToRefreshViewPager) mOpenMainActivity.findViewById(R.id.pull_refresh_viewpager);
    }

    public void testPullViewPager() {
        assertOnScreen(mOpenMainActivity.getWindow().getDecorView(), mPullViewPager);
    }
}
