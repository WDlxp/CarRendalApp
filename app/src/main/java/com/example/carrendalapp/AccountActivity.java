package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

/**
 * @author WD
 */
public class AccountActivity extends AppCompatActivity {

    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil=new ActionBarAndStatusBarUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("账号信息");
        actionBarAndStatusBarUtil.showBackButton();
    }
}
