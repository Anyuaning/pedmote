package com.anyuaning.osp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by thom on 13-10-6.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    List<Fragment> listFragment;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int i) {
        return listFragment.get(i);
    }

    @Override
    public int getCount() {
        if (null == listFragment) {
            return 0;
        } else {
            return listFragment.size();
        }
    }

    @Override
    public int getIconResId(int index) {
        return 0;
    }
}
