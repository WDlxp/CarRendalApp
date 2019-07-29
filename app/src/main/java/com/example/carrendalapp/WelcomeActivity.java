package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 欢迎页面
 *
 * @author WD
 */
public class WelcomeActivity extends AppCompatActivity {
    /**
     * 设置欢迎页等待延迟时间
     */
    private static final int TIME = 1000;

    /**
     * 设置进入引导页还是主界面的标志
     */
    private static final int GO_HOME = 1000;
    private static final int GO_REGISTER = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        init();
    }

    /**
     * 通过Handler来跳转到不同页面(由于等待延迟在主线程中进行是不合理的因此这边使用Handler)
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_REGISTER:
                    goLogin();
                    break;
                default:
            }
        }
    };

    /**
     * 使用SharedPreferences获取account值判断是否已有账号
     */
    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        String account = sharedPreferences.getString("account", null);
        //如果账号为空则说明无登录账号此时进入注册页面
        if (account == null) {
            mHandler.sendEmptyMessageDelayed(GO_REGISTER, TIME);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_HOME, TIME);
        }
    }

    /**
     * 去首页
     */
    private void goHome() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        //关闭当前页面
        finish();
    }

    /**
     * 去登录页
     */
    private void goLogin() {
        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
