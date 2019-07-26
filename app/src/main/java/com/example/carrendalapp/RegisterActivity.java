package com.example.carrendalapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrendalapp.config.UrlAddress;
import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 注册页面
 *
 * @author WD
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivProfile;
    private TextView tvToLogin;
    private Spinner spGender;
    private String[] genderData = new String[]{"男", "女", "保密"};
    private EditText etAccount, etPassword, etPasswordAgain, etName, etTel;
    private Button btnRegister;
    private String imagePath = null;
    private String imageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化标题栏与状态栏
        ActionBarAndStatusBarUtil.initActionBarAndStatusBar(getWindow(), getSupportActionBar());
        ActionBarAndStatusBarUtil.setTitle("注册页");

        //绑定控件
        findViews();
        setListeners();

        //初始化View
        initViews();
    }

    private void initViews() {
        //适配器
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderData);
        //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spGender.setAdapter(arrayAdapter);
    }

    private void setListeners() {
        ivProfile.setOnClickListener(this);
        tvToLogin.setOnClickListener(this);

        btnRegister.setOnClickListener(this);
        //监听账号EditText的焦点，失去焦点的时候检查账号是否存在
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
            if (integer == 0) {
                Toast.makeText(RegisterActivity.this, "账号已存在", Toast.LENGTH_SHORT).show();
                etAccount.requestFocus();
            }
        }
    }

    private void findViews() {
        ivProfile = findViewById(R.id.iv_profile);
        tvToLogin = findViewById(R.id.tv_to_login);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etPasswordAgain = findViewById(R.id.et_password_again);
        etName = findViewById(R.id.et_name);
        spGender = findViewById(R.id.sp_gender);
        etTel = findViewById(R.id.et_tel);

        btnRegister = findViewById(R.id.btn_register);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            //获取图片路径
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor cursor = null;
                if (selectedImage != null) {
                    cursor = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                        imagePath = cursor.getString(columnIndex);
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        ivProfile.setImageBitmap(bitmap);
                        cursor.close();
                    }
                }
                if (imagePath != null) {
                    new UploadImageTask().execute(imagePath);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_profile:
                //设置隐式的Intent访问系统相册并选取一张图片
                //调用相册
                intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.tv_to_login:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_register:
                checkAndRegister();
                break;
            default:
        }
    }

    private void checkAndRegister() {
        String account = etAccount.getText().toString();
        String password = etPassword.getText().toString();
        String passwordAgain = etPasswordAgain.getText().toString();
        String name = etName.getText().toString();
        int gender = spGender.getSelectedItemPosition();
        String tel = etTel.getText().toString();
        //判断数据的完整性
        if (imagePath == null) {
            Toast.makeText(RegisterActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
        } else if (account.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
            //跳转到数据为空处方便用户填写
            etAccount.requestFocus();
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            etPassword.requestFocus();
        } else if (passwordAgain.isEmpty() || !passwordAgain.equals(password)) {
            Toast.makeText(RegisterActivity.this, "密码不一致", Toast.LENGTH_SHORT).show();
            etPasswordAgain.requestFocus();
        } else if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            etName.requestFocus();
        } else if (tel.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
            etTel.requestFocus();
        } else if (tel.length() != 11) {
            Toast.makeText(RegisterActivity.this, "电话号码不正确", Toast.LENGTH_SHORT).show();
            etTel.requestFocus();
        } else {
//            Toast.makeText(RegisterActivity.this, "信息验证成功", Toast.LENGTH_SHORT).show();
            final User user = new User(imageName, account, password, name, gender, tel, 1);
            new InsertUserTask().execute(user);
        }
    }

    /**
     * 插入用户的Task
     */
    private class InsertUserTask extends AsyncTask<User, Void, Integer> {
        private User user = null;

        @Override
        protected Integer doInBackground(User... users) {
            user = users[0];
            URL url = null;
            int result = 0;
            try {
                url = new URL(UrlAddress.INSERT_USER_URL + "?image=" + user.getImageName() + "&account=" + user.getAccount() + "&password=" + user.getPassword() + "&name=" + user.getName() + "&gender=" + user.getGender() + "&tel=" + user.getTel());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("Add Data", url.toString());
                InputStream inputStream = urlConnection.getInputStream();
                //缓冲区结合
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                JSONObject object = new JSONObject(line);
                result = object.getInt("result");
                if (result > 0) {
                    Log.d("ChangeData", "数据添加到后台数据库成功");
                }
                bufferedReader.close();
                inputStream.close();
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
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("imageName", imageName);
                intent.putExtra("account", user.getAccount());
                intent.putExtra("password", user.getPassword());
                startActivity(intent);
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败请再尝试一次", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 上传图片的Task
     */
    private class UploadImageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String imagePath = strings[0];
            //这里的path就是那个地址的全局变量
            File file = new File(imagePath);
            String requestUrl = UrlAddress.UPLOAD_IMAGE_URL;
            //                        String imagePath = URL.BASE_URL + result;
//                        Bitmap bitmap=ImageUtil.downloadImg(imagePath);
            return ImageUtil.uploadFile(file, requestUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(RegisterActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
                imageName = s;
            }
            super.onPostExecute(s);
        }
    }
}
