package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author WD
 */
public class ForgetPasswordActivity extends AppCompatActivity {

    private TextView tvTel, tvResetPassword;
    private EditText etInputTel, etPassword, etPasswordAgain;
    private String account, tel;
    private Button btnCheckTel, btnResetPassword;
    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    private ProgressBar pbResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        //初始化标题栏和状态栏
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("找回密码");
        //显示 返回上一页面的按钮
        actionBarAndStatusBarUtil.showBackButton();

        findViews();
        setListeners();
        initViews();
    }

    private void setListeners() {
        btnCheckTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTel = etInputTel.getText().toString();
                //但输入为空或者不足11位时提示用户
                if (inputTel.isEmpty() || inputTel.length() != 11) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入完整的预留号码", Toast.LENGTH_SHORT).show();
                } else {
                    //如果输入正确的预留手机号
                    if (tel.equals(inputTel)) {
                        //将重置密码的界面显示出来
                        etPassword.setVisibility(View.VISIBLE);
                        etPasswordAgain.setVisibility(View.VISIBLE);
                        btnResetPassword.setVisibility(View.VISIBLE);

                        //禁止输入框和按钮的点击
                        etInputTel.setFocusable(false);
                        btnCheckTel.setEnabled(false);
                    } else {
                        //输入不正确进行提醒
                        Toast.makeText(ForgetPasswordActivity.this, "输入号码与预留号码不匹配", Toast.LENGTH_SHORT).show();
                        etInputTel.requestFocus();
                    }
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = etPassword.getText().toString();
                String newPasswordAgain = etPasswordAgain.getText().toString();
                //对输入的新密码进行逻辑判断
                if (newPassword.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "请设置新密码", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                } else if (newPasswordAgain.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "请确认新密码", Toast.LENGTH_SHORT).show();
                    etPasswordAgain.requestFocus();
                } else if (!newPassword.equals(newPasswordAgain)) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
                    etPasswordAgain.requestFocus();
                } else {
                    new UpdatePasswordTask().execute(account, newPassword);
                }
            }
        });
    }

    private void findViews() {
        tvTel = findViewById(R.id.tv_tel);
        etInputTel = findViewById(R.id.et_input_tel);
        btnCheckTel = findViewById(R.id.btn_check_tel);

        etPassword = findViewById(R.id.et_password);
        etPasswordAgain = findViewById(R.id.et_password_again);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        tvResetPassword = findViewById(R.id.tv_reset_password);
        pbResetPassword = findViewById(R.id.pb_reset_password);
    }

    private void initViews() {
        //获取到账号与电话信息
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        tel = intent.getStringExtra("tel");
        //隐藏号码中间几位
        assert tel != null;
        String hideTel = tel.substring(0, 3) + "*****" + tel.substring(8);
        tvTel.setText(hideTel);
    }

    /**
     * 更新修改密码的Task
     */
    private class UpdatePasswordTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //显示重置中的提示
            btnResetPassword.setVisibility(View.VISIBLE);
            tvResetPassword.setVisibility(View.VISIBLE);
            //进行重置前禁止修改内容
            btnResetPassword.setClickable(false);
            //禁止修改EditText的内容
            etPassword.setFocusable(false);
            etPasswordAgain.setFocusable(false);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            //后台获取传入的账号和新的密码
            String account = strings[0];
            String newPassword = strings[1];

            URL url = null;
            try {
                //获取插入账号的URL
                url = new URL(UrlAddress.FORGET_PASSWORD_URL + "?operation=update&account=" + account + "&newPassword=" + newPassword);
                //获取连接
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //获取输入流
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                return object.getInt("result");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //隐藏加载的提示
            pbResetPassword.setVisibility(View.GONE);
            tvResetPassword.setVisibility(View.GONE);
            if (integer == 1) {
                Toast.makeText(ForgetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "密码重置失败，请重试", Toast.LENGTH_SHORT).show();
                btnResetPassword.setClickable(true);
                etPassword.setFocusable(true);
                etPassword.setFocusableInTouchMode(true);
                etPasswordAgain.setFocusable(true);
                etPasswordAgain.setFocusableInTouchMode(true);
            }
        }
    }
}
