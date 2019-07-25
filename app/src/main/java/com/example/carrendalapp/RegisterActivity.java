package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

/**
 * 注册页面
 * @author WD
 */
public class RegisterActivity extends AppCompatActivity {

    private ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("注册页");
    }
}
