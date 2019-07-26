package com.example.carrendalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private TextView tvTel;
    private EditText etInputTel, etPassword, etPasswordAgain;
    private String account, tel;
    private Button btnCheckTel, btnResetPassword;
    private ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("找回密码");
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
    }

    private void initViews() {
        //获取到账号与电话信息
        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        tel = intent.getStringExtra("tel");
        //隐藏号码中间几位
        String hideTel = tel.substring(0, 3) + "*****" + tel.substring(8);
        tvTel.setText(hideTel);
    }

    private class UpdatePasswordTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String account = strings[0];
            String newPassword = strings[1];

            URL url = null;
            try {
                url = new URL(UrlAddress.FORGET_PASSWORD_URL + "?operation=update&account=" + account + "&newPassword=" + newPassword);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

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
            if (integer == 1) {
                Toast.makeText(ForgetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
