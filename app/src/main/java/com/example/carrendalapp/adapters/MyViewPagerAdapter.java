package com.example.carrendalapp.adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的一个PagerAdapter
 *
 * @author WD
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList;
    private ArrayList<String> pageTitles;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, ArrayList<String> pageTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pageTitles = pageTitles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }
}