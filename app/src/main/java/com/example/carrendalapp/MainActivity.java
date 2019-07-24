package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.carrendalapp.adapter.MainViewPagerAdapter;
import com.example.carrendalapp.fragments.HomePageFragment;
import com.example.carrendalapp.fragments.MemberFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initFragmentList();
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mainViewPagerAdapter);
        setListeners();

        ActionBar actionBar = getSupportActionBar();
        //使用自定义的标题栏
        if(actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.title_layout);
            tvTitle = (TextView) actionBar.getCustomView().findViewById(R.id.display_title);
            tvTitle.setText("租车");
            actionBar.setDisplayShowCustomEnabled(true);
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
