package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.carrendalapp.adapters.MyViewPagerAdapter;
import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.fragments.HomePageFragment;
import com.example.carrendalapp.fragments.MemberFragment;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;
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
    private Bitmap profileImage = null;

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

        //获取传递过来的账号数据
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            //说明数据传递成功
            User user = bundle.getParcelable("data");
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            //存储数据
            editor.putString("imageName", user.getImageName());
            editor.putString("account", user.getAccount());
            editor.putString("password", user.getPassword());
            editor.putString("name", user.getName());
            editor.putInt("gender", user.getGender());
            editor.putString("tel", user.getTel());
            editor.putInt("manager", user.getManager());
            //提交存储放回是否成功
            boolean flag = editor.commit();
            if (flag) {
//                Toast.makeText(MainActivity.this, "数据存储成功", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(MainActivity.this, "数据存储失败", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(MainActivity.this, user.getAccount() + user.getName(), Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(MainActivity.this, "是空的", Toast.LENGTH_LONG).show();
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
    }

    private void findViews() {
        bottomNavigationView = findViewById(R.id.bnv);
        viewPager = findViewById(R.id.vp_container);
    }
}
