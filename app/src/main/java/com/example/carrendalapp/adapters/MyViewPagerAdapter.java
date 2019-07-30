package com.example.carrendalapp.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

    /**
     * 我的预约页面使用
     *
     * @param fm           FragmentManager
     * @param fragmentList Fragment页面的集合
     * @param pageTitles   ViewPager的标题集合
     */
    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, ArrayList<String> pageTitles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.pageTitles = pageTitles;
    }

    /**
     * 首页使用
     *
     * @param fm           FragmentManager
     * @param fragmentList Fragment页面的集合
     */
    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    /**
     * 绑定TabLayout后Tab的text从这里获取
     *
     * @param position 位置
     * @return 返回标题
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        //销毁页面
        container.removeView(fragmentList.get(position).getView());
    }
}
