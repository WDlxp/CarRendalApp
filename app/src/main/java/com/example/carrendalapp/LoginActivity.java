package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;
import com.example.carrendalapp.views.CircleImageView;

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
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvToRegister, tvForgetPassword;
    private Button btnLogin;
    private CircleImageView civProfile;
    private EditText etAccount, etPassword;
    private ProgressBar pbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化标题栏和状态栏
        ActionBarAndStatusBarUtil actionBarAndStatusBarUtil = new ActionBarAndStatusBarUtil();
        actionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        actionBarAndStatusBarUtil.setTitle("登录页");

        findViews();
        setListeners();
        //初始化页面
        initViews();
    }

    private void initViews() {
        Intent intent = getIntent();
        String imageName = intent.getStringExtra("imageName");
        String account = intent.getStringExtra("account");
        String password = intent.getStringExtra("password");
//        Toast.makeText(LoginActivity.this, "接收到的信息：" + imageName + account + password, Toast.LENGTH_SHORT).show();
        if (imageName != null) {
            new DownloadImageTask().execute(imageName);
        }
        if (account != null) {
            etAccount.setText(account);
            etPassword.setText(password);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (strings[0] != null) {
                String imagePath = UrlAddress.BASE_URL + strings[0];
                return ImageUtil.downloadImg(imagePath);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                civProfile.setImageBitmap(bitmap);
            }
        }
    }

    private void setListeners() {
        tvToRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    final String account = etAccount.getText().toString();
                    if (!account.isEmpty()) {
                        new CheckAccountTask().execute(account);
                    }
                }
            }
        });

        tvForgetPassword.setOnClickListener(this);
    }

    private void findViews() {
        tvToRegister = findViewById(R.id.tv_to_register);
        btnLogin = findViewById(R.id.btn_login);
        civProfile = findViewById(R.id.iv_profile);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);

        tvForgetPassword = findViewById(R.id.tv_to_forget_password);
        pbLogin = findViewById(R.id.pb_login);
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
                String account = etAccount.getText().toString();
                String password = etPassword.getText().toString();
                if (account.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    etAccount.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    etAccount.requestFocus();
                } else {
                    new CheckPasswordTask().execute(account, password);
                }
                break;
            case R.id.tv_to_forget_password:
                String account1 = etAccount.getText().toString();
                if (account1.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    etAccount.requestFocus();
                } else {
                    new ForgetPasswordTask().execute(account1);
                }
                break;
            default:

        }
    }

    /**
     * 检查账号密码是否正确Task
     */
    private class CheckPasswordTask extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //设置登陆中的提示界面
            pbLogin.setVisibility(View.VISIBLE);
            btnLogin.setText("登录中...");

            //设置禁止点击防止误触
            btnLogin.setClickable(false);
            etAccount.setFocusable(false);
            etPassword.setFocusable(false);
        }

        @Override
        protected User doInBackground(String... strings) {
            String account = strings[0];
            String password = strings[1];
            User user = null;
            //进行后台操作
            try {
                URL url = new URL(UrlAddress.CHECK_PASSWORD_URL + "?account=" + account + "&password=" + password);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Add Data", url.toString());
                InputStream inputStream = urlConnection.getInputStream();
                //缓冲区结合
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                int result = object.getInt("result");
                //如果获得的result=1说明成功登录
                if (result == 1) {
                    //获取账号信息
                    JSONObject data = object.getJSONObject("data");
                    user = new User(
                            data.getString("imageName"),
                            data.getString("account"),
                            data.getString("password"),
                            data.getString("name"),
                            data.getInt("gender"),
                            data.getString("tel"),
                            data.getInt("manager")
                    );
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            //完成登陆的提示界面
            pbLogin.setVisibility(View.GONE);
            btnLogin.setText("登录");
            if (user != null) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                //重启点击方便用户输入
                btnLogin.setClickable(true);

                etAccount.setFocusable(true);
                etAccount.setFocusableInTouchMode(true);

                etPassword.setFocusable(true);
                etPassword.setFocusableInTouchMode(true);
                etPassword.requestFocus();
            }
        }
    }

    /**
     * 检查账号是否已经存在的Task
     */
    private class CheckAccountTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            String account = strings[0];
            //进行后台操作
            int result = 0;
            try {
                URL url = new URL(UrlAddress.CHECK_USER_URL + "?account=" + account);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Add Data", url.toString());
                InputStream inputStream = urlConnection.getInputStream();
                //缓冲区结合
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                result = object.getInt("result");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Toast.makeText(LoginActivity.this, "账号不存在", Toast.LENGTH_SHORT).show();
                etAccount.requestFocus();
            }
        }
    }

    /**
     * 忘记密码的Task
     */
    private class ForgetPasswordTask extends AsyncTask<String, Void, String> {
        private String account;

        @Override
        protected String doInBackground(String... strings) {
            account = strings[0];
            try {
                URL url = new URL(UrlAddress.FORGET_PASSWORD_URL + "?operation=query&account=" + account);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                int result = object.getInt("result");
                if (result == 0) {
                    return null;
                } else {
                    return object.getString("tel");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                //如果正确获取到账号的号码则跳转到忘记密码
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("account", account);
                intent.putExtra("tel", s);
                startActivity(intent);
            }
        }
    }

}
