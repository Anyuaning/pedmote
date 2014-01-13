package com.anyuaning.osp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by thom on 13-10-6.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> listFragment;

    public TabPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> listFragment) {
        super(supportFragmentManager);
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
            return  listFragment.size();
        }
    }
}
