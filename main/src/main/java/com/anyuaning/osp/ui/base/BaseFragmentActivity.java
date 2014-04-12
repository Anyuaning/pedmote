package com.anyuaning.osp.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.anyuaning.osp.R;
import com.anyuaning.osp.ui.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class BaseFragmentActivity extends SlidingFragmentActivity {

    private Fragment mLeftFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);
        initSlidingMenu(savedInstanceState);
    }

    protected void initSlidingMenu(Bundle savedInstanceState) {
        // init sliding menu
        setBehindContentView(R.layout.left_menu_frame);
        if (null == savedInstanceState) {
            FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
            mLeftFragment = new LeftMenuFragment();
            fragmentTrans.replace(R.id.left_menu, mLeftFragment);
//            fragmentTrans.commit();
            fragmentTrans.commitAllowingStateLoss();
        } else {
            mLeftFragment = getSupportFragmentManager().findFragmentById(R.id.left_menu);
        }

        // sliding menu
//        SlidingMenu slidingMenu = new SlidingMenu(this);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//        slidingMenu.setShadowWidth(30);
//        slidingMenu.setSelectorDrawable();
//        slidingMenu.setBehindOffset(60);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setMenu(R.layout.left_menu_frame);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    };
}
