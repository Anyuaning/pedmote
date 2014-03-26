package com.anyuaning.osp.test;

import android.view.View;


import com.anyuaning.osp.ui.widget.PullToRefreshDragSortListView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import static org.fest.assertions.api.ANDROID.assertThat;


/**
* Created by thom on 13-10-31.
*/
@RunWith(RobolectricGradleTestRunner.class)
public class OpenMainActivityRoboTest {

//    @Test
//    public void testTv() {
//        Activity activity = Robolectric.buildActivity(OpenMainActivity.class).create().get();
//        TextView tv = (TextView) activity.findViewById(R.id.hello);
//        String hel = tv.getText().toString();
//
//        assertThat(hel, equalTo("hellworld"));
//    }

    @Test
    public void testPullDragView() {
        PullToRefreshDragSortListView plv = new PullToRefreshDragSortListView(Robolectric.application);

        int width = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
        int height = View.MeasureSpec.makeMeasureSpec(200, View.MeasureSpec.EXACTLY);

        plv.measure(width, height);

        assertThat(plv).hasMeasuredWidth(100);
        assertThat(plv).hasMeasuredHeight(200);

    }
}
