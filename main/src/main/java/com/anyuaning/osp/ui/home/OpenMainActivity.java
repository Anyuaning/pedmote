package com.anyuaning.osp.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.anyuaning.osp.R;
import com.anyuaning.osp.ui.adapter.TabFragmentPagerAdapter;
import com.anyuaning.osp.ui.adapter.TabPagerAdapter;
import com.anyuaning.osp.ui.fragment.ItemFragment;
import com.anyuaning.osp.ui.fragment.PullListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.viewpager.PullToRefreshViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class OpenMainActivity extends BaseFragmentActivity implements
        ActionBar.TabListener, ViewPager.OnPageChangeListener, PullToRefreshBase.OnRefreshListener {

    private String[] tabTitles;

    private PullToRefreshViewPager mPullToRefreshViewPager;

    private ViewPager mViewPager;

    private TabPagerAdapter mTabPagerAdapter;

    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;

    private ActionBar mActionBar;

    private List<Fragment> mListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock_Light);
        super.onCreate(savedInstanceState);

        setBehindContentView(R.layout.left_menu);
        setContentView(R.layout.activity_openmain);

        setupData();
        setupView();
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
        slidingMenu.setMenu(R.layout.left_menu);

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
