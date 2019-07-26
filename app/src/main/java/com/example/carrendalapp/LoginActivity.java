package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

/**
 * @author WD
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvToRegister;

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
    }

    private void findViews() {
        tvToRegister = findViewById(R.id.tv_to_register);
    }

    @Override
    public void onClick(View view) {
        //跳转都注册页面
        if (view.getId() == R.id.tv_to_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
