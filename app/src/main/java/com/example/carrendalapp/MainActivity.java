package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.carrendalapp.adapters.MyViewPagerAdapter;
import com.example.carrendalapp.entity.User;
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
    private ArrayList<String> titles;

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");
        //绑定布局
        findViews();
        //将Fragment放入List中
        initFragmentList();

        //设置监听事件
        setListeners();

        initViews();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews() {
        //获取一个自定义的ViewPagerAdapter
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        //设置Adapter
        viewPager.setAdapter(myViewPagerAdapter);

        //初始化ActionBar和Status使得状态栏同时标题栏居中的工具类
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("租车");
        //设置底部导航栏的背景颜色与阴影效果
        bottomNavigationView.setElevation(1f);
        bottomNavigationView.setBackgroundColor(Color.WHITE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            User user = bundle.getParcelable("data");
            Toast.makeText(MainActivity.this, user.getAccount() + user.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "是空的", Toast.LENGTH_LONG).show();
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
                    actionBarAndStatusBarUtil.setTitle("租车");
                } else {
                    actionBarAndStatusBarUtil.setTitle("我的");
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

//        titles = new ArrayList<>(fragmentList.size());
//        titles.add("首页");
//        titles.add("我的");
    }

    private void findViews() {
        bottomNavigationView = findViewById(R.id.bnv);
        viewPager = findViewById(R.id.vp_container);
    }
}
