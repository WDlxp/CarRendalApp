package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

/**
 * @author WD
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvToRegister;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("登录页");

        findViews();
        setListeners();
    }

    private void setListeners() {
        tvToRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void findViews() {
        tvToRegister = findViewById(R.id.tv_to_register);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_to_register:
                //跳转到注册页面
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_login:
                //登录进入主页
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:

        }
    }
}
