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

    private TextView tvTitle;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化ActionBar和Status使得状态栏沉浸的同时标题栏居中
        initActionBarAndStatusBar();
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
    }

    private void initActionBarAndStatusBar() {
        ActionBar actionBar = getSupportActionBar();
        //使用自定义的标题栏使得标题栏字体居中
        if (actionBar != null) {
            //设置自定义的ActionBar显示方式
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //设置自定义ActionBar的布局
            actionBar.setCustomView(R.layout.title_layout);
            //设置阴影大小
            actionBar.setElevation(5f);
            //绑定Title
            tvTitle = actionBar.getCustomView().findViewById(R.id.display_title);
            //展示自定义ActionBar
            actionBar.setDisplayShowCustomEnabled(true);
        }
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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
                    tvTitle.setText("租车");
                } else {
                    tvTitle.setText("我的");
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
