package com.example.carrendalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.carrendalapp.config.URL;
import com.example.carrendalapp.entity.User;
import com.example.carrendalapp.utils.ActionBarAndStatusBarUtil;
import com.example.carrendalapp.utils.ImageUtil;

import java.io.File;

/**
 * 注册页面
 *
 * @author WD
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivProfile;
    private TextView tvToLogin;
    private Handler mHandler = new ImageHandler();
    private Spinner spGender;
    private String[] genderData = new String[]{"男", "女", "保密"};
    private EditText etAccount, etPassword, etPasswordAgain, etName, etTel;
    private Button btnRegister;
    private String imagePath = null;

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
                    final String finalImagePath = imagePath;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //这里的path就是那个地址的全局变量
                            File file = new File(finalImagePath);
                            String RequestURL = URL.UPLOAD_IMAGE_URL;
                            String result = ImageUtil.uploadFile(file, RequestURL);
//                        String imagePath = URL.MAIN_URL + result;
//                        Bitmap bitmap=ImageUtil.downloadImg(imagePath);
                            Message message = new Message();
                            message.what = 0;
                            mHandler.sendMessage(message);
                            Log.d("UploadImage", result);
                        }
                    }.start();
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
//        if (imagePath == null) {
//            Toast.makeText(RegisterActivity.this, "请选择头像", Toast.LENGTH_SHORT).show();
//        } else
        Toast.makeText(RegisterActivity.this, "性别代码："+gender, Toast.LENGTH_SHORT).show();
        if (account.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterActivity.this, "信息验证成功", Toast.LENGTH_SHORT).show();
            User user = new User(imagePath, account, password, name, gender, tel, 1);
        }
    }

    @SuppressLint("HandlerLeak")
    private class ImageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Toast.makeText(RegisterActivity.this, "图片上传成功", Toast.LENGTH_SHORT).show();
////                String imagePath = URL.MAIN_URL + msg.obj;
//                Bitmap bitmap= (Bitmap) msg.obj;
////                Log.d("ImagePath",imagePath);
//                if (bitmap!=null){
//                    ivProfile.setImageBitmap(bitmap);
//                    Log.d("Image","图片不为空");
//                }else {
//                    Log.d("Image","图片为空");
//                }
            }
        }
    }

}
