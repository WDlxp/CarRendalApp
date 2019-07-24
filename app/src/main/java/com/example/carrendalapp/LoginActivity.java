package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

/**
 * @author WD
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(),getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("注册页");
    }
}
