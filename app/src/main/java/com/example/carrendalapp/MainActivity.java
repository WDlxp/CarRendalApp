package com.example.carrendalapp;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.carrendalapp.adapters.MainViewPagerAdapter;
import com.example.carrendalapp.fragments.HomePageFragment;
import com.example.carrendalapp.fragments.MemberFragment;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WD
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private HomePageFragment homePageFragment = new HomePageFragment();
    private MemberFragment memberFragment = new MemberFragment();
    private List<Fragment> fragmentList = new ArrayList<>(2);

    private ViewPager viewPager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定布局
        findViews();
        //将Fragment放入List中
        initFragmentList();

        //获取一个自定义的ViewPagerAdapter
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置Adapter
        viewPager.setAdapter(mainViewPagerAdapter);

        //设置监听事件
        setListeners();
        //初始化ActionBar和Status使得状态栏同时标题栏居中的工具类
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("租车");
    }

    private void setListeners() {
        //设置viewPager监听来同步BottomNavigationView的选中效果
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当页面发生改变时，更改BottomNavigationView的选中状态
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                //修改标题栏
                if (position == 0) {
                    ActionBarAndStatusBarUtil.setTitle("租车");
                } else {
                    ActionBarAndStatusBarUtil.setTitle("我的");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置bottomNavigationView的点击导航到ViewPager的对应页面
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_home) {
                    viewPager.setCurrentItem(0);
                } else if (menuItem.getItemId() == R.id.navigation_member) {
                    viewPager.setCurrentItem(1);
                }
                return true;
            }
        });
    }

    /**
     * 初始化FragmentList
     */
    private void initFragmentList() {
        fragmentList.add(homePageFragment);
        fragmentList.add(memberFragment);
    }

    private void findViews() {
        bottomNavigationView = findViewById(R.id.bnv);
        viewPager = findViewById(R.id.vp_container);
    }
}
