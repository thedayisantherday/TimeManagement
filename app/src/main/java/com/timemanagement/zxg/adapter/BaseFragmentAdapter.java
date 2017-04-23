package com.timemanagement.zxg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 2016/11/7.
 * QQ:1092885570
 */

public class BaseFragmentAdapter extends FragmentPagerAdapter {

    protected List<Fragment> mFragments;
    protected String[] mTabTitles;

    public BaseFragmentAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitles) {
        super(fm);
        if (fragments == null){
            mFragments = new ArrayList<>();
        }
        this.mFragments = fragments;
        this.mTabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return isFragmentsEmpty() ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return isFragmentsEmpty() ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (mTabTitles==null || mTabTitles.length<=position) ? "" : mTabTitles[position];
    }

    private boolean isFragmentsEmpty(){
        return mFragments == null;
    }
}
