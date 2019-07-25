package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;

import com.example.carrendalapp.adapters.MyViewPagerAdapter;
import com.example.carrendalapp.fragments.AllFragment;
import com.example.carrendalapp.fragments.AppointingFragment;
import com.example.carrendalapp.fragments.FinishedFragment;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的预约页面
 *
 * @author WD
 */
public class MyAppointmentActivity extends AppCompatActivity {

    private ViewPager vpContainer;
    private AllFragment allFragment = new AllFragment();
    private FinishedFragment finishedFragment = new FinishedFragment();
    private AppointingFragment appointingFragment = new AppointingFragment();
    private List<Fragment> fragmentList = new ArrayList<>();

    private TabLayout tlTabs;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment);
        findViews();
        initFragmentList();
        initViews();
    }

    /**
     * 初始化Tab页面
     */
    private void initViews() {
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("我的预约");
        ActionBarAndStatusBarUtil.showBackButton();

        //设置ViewPager的Title之后TabLayout绑定后通过获取PageTitle作为Tab的文字
        titles = new ArrayList<>(fragmentList.size());
        titles.add(" 全部 ");
        titles.add("预约中");
        titles.add("已结束");
        //获取ViewPager的适配器
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        vpContainer.setAdapter(myViewPagerAdapter);
        //TabLayout绑定ViewPager
        tlTabs.setupWithViewPager(vpContainer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    private void findViews() {
        vpContainer = findViewById(R.id.vp_container);
        tlTabs = findViewById(R.id.tl_tabs);
    }

    private void initFragmentList() {
        fragmentList.add(allFragment);
        fragmentList.add(appointingFragment);
        fragmentList.add(finishedFragment);
    }
}
