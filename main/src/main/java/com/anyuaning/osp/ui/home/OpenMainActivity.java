package com.anyuaning.osp.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.anyuaning.osp.R;
import com.anyuaning.osp.config.OspAction;
import com.anyuaning.osp.jnimp3lame.TestNativeMain;
import com.anyuaning.osp.ui.adapter.TabFragmentPagerAdapter;
import com.anyuaning.osp.ui.adapter.TabPagerAdapter;
import com.anyuaning.osp.ui.base.BaseFragmentActivity;
import com.anyuaning.osp.ui.fragment.ItemFragment;
import com.anyuaning.osp.ui.fragment.PullListFragment;
import com.anyuaning.osp.ui.fragment.sport.CountStepFragment;
import com.anyuaning.osp.ui.fragment.sport.SportMainFragment;
import com.anyuaning.osp.ui.fragment.sport.StopWatchFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.viewpager.PullToRefreshViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class OpenMainActivity extends BaseFragmentActivity implements
        ActionBar.TabListener, ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener, CountStepFragment.OnFragmentInteractionListener {

    private String[] tabTitles;

    private PullToRefreshViewPager mPullToRefreshViewPager;

    private ViewPager mViewPager;

    private TabPagerAdapter mTabPagerAdapter;

    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;

    private ActionBar mActionBar;

    private List<Fragment> mListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_openmain);
//
//        setupData();
//        setupView();
//        setupStopWatch();

        switchContent(new SportMainFragment());

        // test
        TestNativeMain tnm = new TestNativeMain();
//        tnm.msg("test native main call native method in openmainactivity"); // can't not find native method
        tnm.printMsg("test native main call native method in openmainactivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent musicIntent = new Intent();
        musicIntent.setAction(OspAction.ACTION_MUSIC_SERVICE);
        stopService(musicIntent);

        System.exit(1);
    }

    private void setupStopWatch() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, StopWatchFragment.class.getName()));
//        fragments.add(Fragment.instantiate(this, CountDownFragment.class.getName()));

        mViewPager = (ViewPager) findViewById(R.id.vp_main);

        PagerAdapter pagerAdapter = new StopWatchPagerAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(pagerAdapter);


    }

    /**
     * switch menu fragment
     * @param fragment
     */
    public void switchContent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

        new Handler() {

        }.postDelayed(new Runnable() {

            @Override
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // count step fragment
    }

    class StopWatchPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragments;

        public StopWatchPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);

            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            if (null == fragments) {
                return 0;
            }
            return fragments.size();
        }


    }

    private void setupView() {

        mPullToRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.pull_refresh_viewpager);
        mPullToRefreshViewPager.setOnRefreshListener(this);
        mViewPager = mPullToRefreshViewPager.getRefreshableView();

        // before mActionBar add, onTabSelected execute
//        mViewPager = (ViewPager) findViewById(R.id.vp_main);
//        mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mListFragment);
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mTabFragmentPagerAdapter);
//        mViewPager.setOnPageChangeListener(this);

        mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        ActionBar.Tab tab = null;
        for (int i=0; i<tabTitles.length; i++) {
            tab = mActionBar.newTab();
            tab.setText(tabTitles[i]);
            tab.setTabListener(this);
            mActionBar.addTab(tab);
        }

        mListFragment.add(new PullListFragment());
        Fragment fragment = null;
        for (int i=1; i<tabTitles.length; i++) {
            fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", tabTitles[i]);
            fragment.setArguments(args);
            mListFragment.add(fragment);
        }

//        mTabPagerAdapter.notifyDataSetChanged();  // need
        mTabFragmentPagerAdapter.notifyDataSetChanged();

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.vp_indicator);
        indicator.setViewPager(mViewPager);
        indicator.setSnap(true);
        indicator.setOnPageChangeListener(this);



    }

    private void setupData() {
        tabTitles = new String[] {"tab1", "tab2", "tab3", "tab4", "tab5"};
        mListFragment = new ArrayList<Fragment>();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("Share");
        subMenu.add("menu1");

        MenuItem menuItem = subMenu.getItem();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ("menu1".equals(item.getTitle())) {
//            AppMsg appMsg = AppMsg.makeText(this, "menu1", AppMsg.STYLE_INFO);
//            appMsg.setLayoutGravity(Gravity.BOTTOM);
//            appMsg.show();
        }
        if (item.getItemId() == android.R.id.home) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        mActionBar.setSelectedNavigationItem(i);
        switch (i) {
            case 0: {
                getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                break;
            }
            default : {
                getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }
}
