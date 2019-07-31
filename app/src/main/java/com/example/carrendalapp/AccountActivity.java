package com.example.carrendalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.tasks.DownloadImageTask;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.views.CircleImageView;

/**
 * @author WD
 */
public class AccountActivity extends AppCompatActivity {

    private CircleImageView civProfile;
    private EditText etAccount, etPassword, etName, etGender, etTel;
    private Button btnLoginOut;
    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("账号信息");
        actionBarAndStatusBarUtil.showBackButton();


        findViews();
        setListeners();
        //初始化页面
        initViews();
    }

    private void setListeners() {
        //退出登录，返回登录页
        btnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清空账号信息
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                //清空账号的数据
                editor.putString("account", null);
                editor.apply();
                //跳转回登录页
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                //设置清空任务栈，无法返回原来的页面
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findViews() {
        civProfile = findViewById(R.id.iv_profile);
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etName = findViewById(R.id.et_name);
        etGender = findViewById(R.id.et_gender);
        etTel = findViewById(R.id.et_tel);

        btnLoginOut = findViewById(R.id.btn_login_out);
    }

    private void initViews() {
        civProfile.setClickable(false);
        etAccount.setFocusable(false);
        etPassword.setFocusable(false);
        etName.setFocusable(false);
        etGender.setFocusable(false);
        etTel.setFocusable(false);

        //获取SharedPreferences
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        //获取账号的数据
        User user = new User(
                sp.getString("imageName", null),
                sp.getString("account", null),
                sp.getString("password", null),
                sp.getString("name", null),
                sp.getInt("gender", 2),
                sp.getString("tel", null),
                sp.getInt("manager", 1)
        );


        //如果图片不为空的时候下载头像
        if (!"null".equals(user.getImageName()) && user.getImageName() != null) {
            Toast.makeText(AccountActivity.this, "下载头像", Toast.LENGTH_SHORT).show();
            new DownloadImageTask(civProfile).execute(user.getImageName());
        }
        //设置账号相关信息
        etName.setText(user.getName());
        etAccount.setText(user.getAccount());
        etPassword.setText(user.getPassword());
        //根据性别编号设置相关性别
        if (user.getGender() == 0) {
            etGender.setText("男");
        } else if (user.getGender() == 1) {
            etGender.setText("女");
        } else {
            etGender.setText("保密");
        }
        etTel.setText(user.getTel());

    }
}
